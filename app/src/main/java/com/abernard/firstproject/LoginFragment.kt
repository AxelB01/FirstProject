package com.abernard.firstproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.abernard.firstproject.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
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

        var binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnLogInUser.setOnClickListener{

            var isValid: Boolean = true

            var email = binding.txtLoginUserName.text.toString()
            isValid = email.isNotEmpty()

            var pass = binding.txtLoginPassword.text.toString()
            isValid = pass.isNotEmpty()

            if (isValid){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            binding.txtLoginUserName.setText("")
                            binding.txtLoginPassword.setText("")
                            //Toast.makeText(root.context, "¡Usuario correcto!", Toast.LENGTH_SHORT).show()
                            toMainActivity(root.context, email)
                        }else{
                            Toast.makeText(root.context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                Toast.makeText(root.context, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnForgotPass.setOnClickListener{
            val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.passRecoveryFragment)
        }

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login, container, false)
        return root
    }

    private fun toMainActivity(context: Context, email: String){
        var userName = email.split("@")[0]

        var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        database.child(userName).get().addOnCompleteListener {
            if(it.isSuccessful && it.result.exists()){
                val data = it.result

                var userName = data.child("userName").value.toString()
                var firstName = data.child("firstName").value.toString()
                var lastName = data.child("lastName").value.toString()
                var birthDate = data.child("birthDate").value.toString()
                var gender = data.child("gender").value.toString()
                var country = data.child("country").value.toString()
                var state = data.child("state").value.toString()
                var address = data.child("address").value.toString()
                var phone = data.child("phone").value.toString()
                var email = data.child("email").value.toString()

                val userModel = UserModel(userName, firstName, lastName, birthDate, gender, country, state, address, phone, email)

                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("model", userModel)
                startActivity(intent)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}