
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidannotation.databinding.FragmentMainBinding
import com.hero.libannotation.FragmentDestination

@FragmentDestination(pageUrl = "tabs/second",needLogin = true, asStarter = false)
class SecondFragment : Fragment() {

    var binding : FragmentMainBinding?=null

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        binding!!.fragmentId.text="Fragment_Second"
        return binding!!.root
    }



}