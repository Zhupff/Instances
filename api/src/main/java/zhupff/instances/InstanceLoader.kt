package zhupff.instances

object InstanceLoader {

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T> load(of: Class<T>, loader: ClassLoader?): List<Class<T>> {
        val classLoader = loader ?: Thread.currentThread().contextClassLoader
        return classLoader.getResources("META-INF/services/${of.canonicalName}")
            .toList()
            .flatMap { it.openStream().reader(Charsets.UTF_8).readLines() }
            .map { Class.forName(it) as Class<T> }
    }

    @JvmStatic
    fun <T> create(of: Class<T>, loader: ClassLoader?): List<T> {
        return load(of, loader).map { it.getConstructor().newInstance() }
    }

    @JvmStatic
    fun <T> create(of: Class<T>, loader: ClassLoader?, condition: (Class<T>) -> Boolean): List<T> {
        return load(of, loader).filter(condition).map { it.getConstructor().newInstance() }
    }

    @JvmStatic
    fun <T> createFirstOrNull(of: Class<T>, loader: ClassLoader?, condition: (Class<T>) -> Boolean): T? {
        return load(of, loader).firstOrNull(condition)?.getConstructor()?.newInstance()
    }

    @JvmStatic
    fun <T> createFirstOrNull(of: Class<T>, loader: ClassLoader?): T? {
        return load(of, loader).firstOrNull()?.getConstructor()?.newInstance()
    }

    @JvmStatic
    fun <T> createLastOrNull(of: Class<T>, loader: ClassLoader?): T? {
        return load(of, loader).lastOrNull()?.getConstructor()?.newInstance()
    }

    @JvmStatic
    fun <T> createLastOrNull(of: Class<T>, loader: ClassLoader?, condition: (Class<T>) -> Boolean): T? {
        return load(of, loader).lastOrNull(condition)?.getConstructor()?.newInstance()
    }
}

inline fun <reified T> loadInstancesOf(loader: ClassLoader? = null): List<Class<T>> = InstanceLoader.load(T::class.java, loader)

inline fun <reified T> createInstancesOf(loader: ClassLoader? = null): List<T> = InstanceLoader.create(T::class.java, loader)

inline fun <reified T> createInstancesOf(loader: ClassLoader? = null, noinline condition: (Class<T>) -> Boolean): List<T> = InstanceLoader.create(T::class.java, loader, condition)

inline fun <reified T> createFirstInstanceOrNull(loader: ClassLoader? = null): T? = InstanceLoader.createFirstOrNull(T::class.java, loader)

inline fun <reified T> createFirstInstanceOrNull(loader: ClassLoader? = null, noinline condition: (Class<T>) -> Boolean): T? = InstanceLoader.createFirstOrNull(T::class.java, loader, condition)

inline fun <reified T> createLastInstanceOrNull(loader: ClassLoader? = null): T? = InstanceLoader.createLastOrNull(T::class.java, loader)

inline fun <reified T> createLastInstanceOrNull(loader: ClassLoader? = null, noinline condition: (Class<T>) -> Boolean): T? = InstanceLoader.createLastOrNull(T::class.java, loader, condition)