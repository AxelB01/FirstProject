package com.abernard.firstproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.abernard.firstproject.databinding.FragmentPassRecoveryBinding
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PassRecoveryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PassRecoveryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = FragmentPassRecoveryBinding.inflate(inflater, container, false)
        val root = binding.root

        binding.btnRecoverPass.setOnClickListener{
            var email = binding.txtUserEmail.text.toString()
            if (email.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            binding.txtUserEmail.setText("")
                            Toast.makeText(root.context, "Mensaje enviado", Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                Toast.makeText(root.context, "Debe ingresar un correo electrónico", Toast.LENGTH_SHORT).show()
            }
        }

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_pass_recovery, container, false)
        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PassRecoveryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PassRecoveryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}