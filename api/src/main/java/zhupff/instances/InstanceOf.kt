package zhupff.instances

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class InstanceOf(vararg val of: KClass<*>)