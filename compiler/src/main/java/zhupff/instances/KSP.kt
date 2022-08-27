package zhupff.instances

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSType

class KSP : SymbolProcessorProvider, SymbolProcessor {

    private val collection = HashMap<String, Pair<HashSet<String>, HashSet<KSFile>>>()

    private lateinit var environment: SymbolProcessorEnvironment

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor = apply {
        this.environment = environment
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getClassDeclarationByName(
            resolver.getKSNameFromString(InstanceOf::class.java.canonicalName)
        )?.asType(emptyList())?.let { annotationType ->
            resolver.getSymbolsWithAnnotation(InstanceOf::class.java.canonicalName)
                .mapNotNull { it as? KSClassDeclaration }
                .forEach { symbol ->
                    (symbol.annotations.find { it.annotationType.resolve() == annotationType }
                        ?.arguments
                        ?.find { it.name?.getShortName() == "of" }
                        ?.value as? Iterable<*>)
                        ?.mapNotNull { it as? KSType }
                        ?.forEach { of ->
                            collection.getOrPut(of.qualifierClassName) {
                                HashSet<String>() to HashSet<KSFile>()
                            }.let {
                                it.first.add(symbol.qualifierClassName)
                                it.second.addIfNonNull(symbol.containingFile)
                            }
                        }
                }
        }
        return emptyList()
    }

    override fun finish() {
        super.finish()
        collection.forEach { (of, instance) ->
            try {
                environment.codeGenerator.createNewFile(
                    Dependencies(true, *instance.second.toTypedArray()),
                    "", "META-INF/services/$of", ""
                ).bufferedWriter(Charsets.UTF_8).use { writer ->
                    instance.first.forEach {
                        writer.write(it)
                        writer.newLine()
                    }
                    writer.flush()
                }
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }

    private val KSType.qualifierClassName: String; get() = "${declaration.packageName.getQualifier()}.${declaration.packageName.getShortName()}.${declaration.simpleName.getShortName()}"

    private val KSClassDeclaration.qualifierClassName: String; get() = "${packageName.getQualifier()}.${packageName.getShortName()}.${simpleName.getShortName()}"

    private fun <T> HashSet<T>.addIfNonNull(item: T?) {
        if (item != null) add(item)
    }
}