package zhupff.instances

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.StandardLocation

@Suppress("UNCHECKED_CAST")
class APT : AbstractProcessor() {

    private val collection = HashMap<String, HashSet<String>>()

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun getSupportedAnnotationTypes(): Set<String> = setOf(InstanceOf::class.java.canonicalName)

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        if (roundEnv?.processingOver() != true) {
            roundEnv?.getElementsAnnotatedWith(InstanceOf::class.java)
                ?.forEach { element ->
                    element.annotationMirrors
                        .find { it.annotationType.asElement().canonicalClassName == InstanceOf::class.java.canonicalName }
                        ?.let { annotation ->
                            annotation.elementValues.forEach { (k, v) ->
                                if (k.simpleName.toString() == "of") {
                                    (v.value as Iterable<AnnotationValue>)
                                        .forEach { of ->
                                            collection.getOrPut(of.value.toString()) {
                                                HashSet()
                                            }.add(element.canonicalClassName)
                                        }
                                }
                            }
                        }
                }
        } else {
            collection.forEach { (of, instances) ->
                processingEnv.filer.createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/services/$of")
                    .openWriter().use { writer ->
                        instances.forEach {
                            writer.appendLine(it)
                        }
                        writer.flush()
                    }
            }
        }
        return false
    }

    private val Element.canonicalClassName; get() = "${processingEnv.elementUtils.getPackageOf(this).qualifiedName}.${simpleName}"
}