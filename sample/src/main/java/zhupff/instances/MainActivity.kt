package zhupff.instances

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            TextView(this).also { tv ->
                tv.gravity = Gravity.CENTER
                tv.text = "See example code of use in sample."
            }, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        )
        sample()
    }

    private fun sample() {
        createInstancesOf<IA>().forEach { Log.i("createInstancesOf", "$it") }
        createInstancesOf<IB> { it != ImplC::class.java }.forEach { Log.i("createInstancesOf", "$it") }
        createFirstInstanceOrNull<IA>().let { Log.i("createFirstInstanceOrNull", "$it") }
        createFirstInstanceOrNull<IB> { it != ImplC::class.java }.let { Log.i("createFirstInstanceOrNull", "$it") }
        createLastInstanceOrNull<IA>().let { Log.i("createLastInstanceOrNull", "$it") }
        createLastInstanceOrNull<IB> { it != ImplC::class.java }.let { Log.i("createLastInstanceOrNull", "$it") }

        readInstancesOf(RuntimeAnnotation::class.java.canonicalName!!).forEach { Log.d("readInstancesOf", it) }
        loadInstancesOf(RuntimeAnnotation::class.java.canonicalName!!).forEach { Log.d("loadInstancesOf", "${it}") }
    }
}



interface IA

interface IB

@InstanceOf(of = [IA::class, IB::class])
class ImplA : IA, IB

@InstanceOf(of = [IA::class, IB::class])
class ImplB : IA, IB

@InstanceOf(of = [IA::class, IB::class])
class ImplC : IA, IB



@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class RuntimeAnnotation

@InstanceOf(of = [RuntimeAnnotation::class])
class ImplD

@InstanceOf(of = [RuntimeAnnotation::class])
class ImplE