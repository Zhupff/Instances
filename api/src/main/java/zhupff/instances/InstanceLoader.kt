package zhupff.instances

@Suppress("UNCHECKED_CAST")
class InstanceLoader(private val classLoader: ClassLoader) {

    constructor(): this(Thread.currentThread().contextClassLoader)

    fun read(of: String): List<String> =
        classLoader
            .getResources("META-INF/services/$of")
            .toList()
            .flatMap { it.openStream().reader(Charsets.UTF_8).readLines() }

    fun read(of: Class<*>): List<String> =
        read(of.canonicalName)

    fun load(of: String): List<Class<*>> =
        read(of).map { Class.forName(it) }

    fun <T> load(of: Class<T>): List<Class<T>> =
        read(of).map { Class.forName(it) as Class<T> }

    fun create(of: String): List<Any> =
        load(of).map { it.getConstructor().newInstance() }

    fun create(of: String, filter: (Class<*>) -> Boolean): List<Any> =
        load(of).filter(filter).map { it.getConstructor().newInstance() }

    fun  createOne(of: String, filter: (List<Class<*>>) -> Class<*>?): Any? =
        filter.invoke(load(of))?.getConstructor()?.newInstance()

    fun <T> create(of: Class<T>): List<T> =
        load(of).map { it.getConstructor().newInstance() }

    fun <T> create(of: Class<T>, filter: (Class<T>) -> Boolean): List<T> =
        load(of).filter(filter).map { it.getConstructor().newInstance() }

    fun <T> createOne(of: Class<T>, filter: (List<Class<T>>) -> Class<T>?): T? =
        filter.invoke(load(of))?.getConstructor()?.newInstance()
}

inline fun <reified T> InstanceLoader.read(): List<String> =
    read(T::class.java)

inline fun <reified T> InstanceLoader.load(): List<Class<T>> =
    load(T::class.java)

inline fun <reified T> InstanceLoader.create(): List<T> =
    create(T::class.java)

inline fun <reified T> InstanceLoader.create(noinline filter: (Class<T>) -> Boolean): List<T> =
    create(T::class.java, filter)

inline fun <reified T> InstanceLoader.createOne(noinline filter: (List<Class<T>>) -> Class<T>?): T? =
    createOne(T::class.java, filter)