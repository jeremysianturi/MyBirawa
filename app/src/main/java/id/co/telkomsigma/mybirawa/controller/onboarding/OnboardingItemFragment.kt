package id.co.telkomsigma.mybirawa.controller.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import id.co.telkomsigma.mybirawa.R
import id.co.telkomsigma.mybirawa.entity.RegisterPreferenceModel
import id.co.telkomsigma.mybirawa.util.RegisterPreference
import kotlinx.android.synthetic.main.fragment_onboarding2.view.*

class OnboardingItemFragment : Fragment() {
    private lateinit var title: String
    private lateinit var description: String
    private var imageResource = 0
    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: AppCompatTextView
    private lateinit var image: ImageView

    // register session
    private lateinit var mRegisterPreference: RegisterPreference
    private lateinit var modelRegisterSession: RegisterPreferenceModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            title =
                arguments?.getString(ARG_PARAM1)!!
            description =
                arguments?.getString(ARG_PARAM2)!!
            imageResource =
                requireArguments().getInt(ARG_PARAM3)
        }
        clearRegisterSession()
    }


    override fun onResume() {
        super.onResume()
        clearRegisterSession()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootLayout: View = inflater.inflate(R.layout.fragment_onboarding2, container, false)
        tvTitle = rootLayout.text_onboarding_title
        tvDescription = rootLayout.text_onboarding_description
        image = rootLayout.image_onboarding
        tvTitle.text = title
        tvDescription.text = description
//        image.drawable = imageResource
        image.setImageResource(imageResource)

        // animation
        val topAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.top_animation)
        val bottomAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_animation)
        val rightAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.right_animation)
        val leftAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.left_animation)
        tvTitle.animation = leftAnim
        tvDescription.animation = rightAnim
        image.animation = topAnim

        return rootLayout
    }

    private fun clearRegisterSession() {
        // register session
        mRegisterPreference = RegisterPreference(requireContext())
        modelRegisterSession = mRegisterPreference.getData()

        modelRegisterSession.nama = ""
        modelRegisterSession.phone = ""
        modelRegisterSession.email = ""
        modelRegisterSession.unitId = ""
        modelRegisterSession.unitName = ""
        modelRegisterSession.suUnit = ""
        modelRegisterSession.cityId = ""
        modelRegisterSession.cityName = ""
        modelRegisterSession.address = ""
        modelRegisterSession.password = ""
        modelRegisterSession.rePassword = ""
        mRegisterPreference.setData(modelRegisterSession)
    }


    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val ARG_PARAM3 = "param3"
        fun newInstance(
            title: String,
            description: String,
            imageResource: Int
        ): OnboardingItemFragment {
            val fragment = OnboardingItemFragment()
            val args = Bundle()

            args.putString(
                ARG_PARAM1,
                title
            )
            args.putString(
                ARG_PARAM2,
                description
            )
            args.putInt(
                ARG_PARAM3,
                imageResource
            )
            fragment.arguments = args
            return fragment
        }
    }
}