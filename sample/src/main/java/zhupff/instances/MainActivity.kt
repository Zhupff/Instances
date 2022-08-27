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
        val instanceLoader = InstanceLoader(application.classLoader)
        instanceLoader.read(RuntimeAnnotation::class.java.canonicalName!!).forEach { Log.d("read instances of RuntimeAnnotation", it) }
        instanceLoader.load(RuntimeAnnotation::class.java.canonicalName!!).forEach { Log.d("read instances of RuntimeAnnotation", "$it") }
        instanceLoader.create<IA>().forEach { Log.i("create instances of IA", "$it") }
        instanceLoader.create<IB> { it != ImplC::class.java }.forEach { Log.i("create instances of IB", "$it") }
        instanceLoader.createOne<IA> { list -> list.find { it == ImplC::class.java } }.let { Log.i("create one instance of IA", "$it") }
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