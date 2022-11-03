package com.abernard.firstproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.abernard.firstproject.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val genders = listOf<String>(
        "Hombre",
        "Mujer",
        "No definir"
    )

    val countries = listOf<String>(
        "Estados Unidos",
        "República Dominicana"
    )

    val rdProvincias = listOf<String>(
        "Azúa", "Baoruco", "Barahona", "Dajabón", "Distrito Nacional", "Duarte", "Elías Pina",
        "El Seibo", "Espaillat", "Hato Mayor", "Independencia", "La Altagracia", "La Romana",
        "La Vega", "María Trinidad Sanchez", "Monseñor Nouel", "Monte Cristi", "Monte Plata",
        "Pedernales", "Peravia", "Puerto Plata", "Salcedo", "Samaná", "Sánchez Ramírez",
        "San Cristobal", "San José de Ocoa", "San Juan", "San Pedro de Macorís", "Santiago",
        "Santiago Rodríguez", "Santo Domingo", "Valverde"
    )

    val usaStates = listOf<String>(
        "Alabama(AL)", "Alaska (AK)", "Arizona (AZ)", "Arkansas (AR)", "California (CA)",
        "Carolina del Norte (NC)", "Carolina del Sur (SC)", "Colorado (CO)", "Connecticut (CT)",
        "Dakota del Norte (ND)", "Dakota del Sur (SD)", "Delaware (DE)", "Florida (FL)", "Georgia (GA)",
        "Hawái (HI)", "Idaho (ID)", "Illinois (IL)", "Indiana (IN)", "Iowa (IA)", "Kansas (KS)",
        "Kentucky (KY)", "Luisiana (LA)", "Maine (ME)", "Maryland (MD)", "Massachusetts (MA)",
        "Míchigan (MI)", "Minnesota (MN)", "Misisipi (MS)", "Misuri (MO)", "Montana (MT)", "Nebraska (NE)",
        "Nevada (NV)", "Nueva Jersey (NJ)", "Nueva York (NY)", "Nuevo Hampshire (NH)", "Nuevo México (NM)",
        "Ohio (OH)", "Oklahoma (OK)", "Oregón (OR)", "Pensilvania (PA)", "Rhode Island (RI)", "Tennessee (TN)",
        "Texas (TX)", "Utah (UT)", "Vermont (VT)", "Virginia (VA)", "Virginia Occidental (WV)",
        "Washington (WA)", "Wisconsin (WI)", "Wyoming (WY)"
    )

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
        //val view = inflater.inflate(R.layout.fragment_registration, container, false)
        var binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val root : View = binding.root

        setItems(root.context, binding.ddlGender, genders)

        setItems(root.context, binding.ddlCountry, countries)
        binding.ddlCountry.setOnItemClickListener { adapterView, view, i, l ->
            val selectedValue = adapterView.getItemAtPosition(i).toString()
            val list = if (selectedValue == "República Dominicana") rdProvincias else usaStates
            binding.ddlState.setText("")
            setItems(root.context, binding.ddlState, list)
        }

        binding.btnSaveRegistration.setOnClickListener{

            binding.btnSaveRegistration.isEnabled = false
            binding.btnSaveRegistration.isClickable = false

            var isValid : Boolean = true

            var firstName = binding.txtName.text.toString()
            isValid = firstName.length != 0

            var lastName = binding.txtLast.text.toString()
            isValid = lastName.length != 0

            var gender = binding.ddlGender.text.toString()
            isValid = gender.length != 0

            var birthDate = binding.txtDate.text.toString()
            isValid = birthDate.length != 0

            var contry = binding.ddlCountry.text.toString()
            isValid = contry.length != 0

            var state = binding.ddlState.text.toString()
            isValid = state.length != 0

            var address = binding.txtAddress.text.toString()
            isValid = address.length != 0

            var phone = binding.txtPhoneNumber.text.toString()
            isValid = phone.length != 0

            var email = binding.txtEmail.text.toString()
            isValid = email.length != 0

            var pass = binding.txtPass.text.toString()
            isValid = pass.length != 0

            var pass2 = binding.txtPass2.text.toString()
            isValid = pass2.length != 0

            if (pass != pass2){
                Toast.makeText(root.context,"Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
            }

            if (isValid){

                var userName = email.split("@")[0]

                var database : DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
                val user = User(userName,firstName, lastName, gender, birthDate, contry, state, address, phone, email, pass)
                database.child(userName).setValue(user)
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful){
                            FirebaseAuth.getInstance()
                                .createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener{
                                    if(it.isSuccessful){
                                        validRegistration(binding)
                                    }
                                }
                        }else{

                            binding.btnSaveRegistration.isEnabled = true
                            binding.btnSaveRegistration.isClickable = true

                            Toast.makeText(root.context,"Ha ocurrido un error...", Toast.LENGTH_LONG).show()
                        }
                    }

            }else{
                Toast.makeText(root.context,"Debe llenar todos los campos", Toast.LENGTH_LONG).show()
            }

            binding.btnSaveRegistration.isEnabled = true
            binding.btnSaveRegistration.isClickable = true
        }
        // Inflate the layout for this fragment
        return root
    }

    fun setItems(context: Context, ddl: AutoCompleteTextView, items: List<String>){
        val adapter = ArrayAdapter(context, R.layout.list_item, items)
        ddl.setAdapter(adapter)
    }

    fun validRegistration(binding: FragmentRegistrationBinding){
        binding.txtName.text.clear()
        binding.txtLast.text.clear()
        binding.txtDate.text.clear()
        binding.txtAddress.text.clear()
        binding.txtEmail.text.clear()
        binding.txtPass.text.clear()
        binding.txtPass2.text.clear()

        binding.ddlGender.setText("")
        binding.ddlCountry.setText("")
        binding.ddlState.setText("")

        binding.btnSaveRegistration.isEnabled = true
        binding.btnSaveRegistration.isClickable = true

        Toast.makeText(binding.root.context,"¡Datos guardados!", Toast.LENGTH_LONG).show()

        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.selectionFragment)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegistrationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistrationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}

