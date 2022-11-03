package com.abernard.firstproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abernard.firstproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        //setContentView(R.layout.activity_main)
        setContentView(root)

        val model = intent.getSerializableExtra("model") as? UserModel

        binding.vwUserName.setText(model?.userName.toString())
        binding.vwFirstName.setText(model?.firstName.toString())
        binding.vwLastName.setText(model?.lastName.toString())
        binding.vwGender.setText(model?.gender.toString())
        binding.vwBirthDate.setText(model?.birthDate.toString())
        binding.vwCountry.setText(model?.country.toString())
        binding.vwState.setText(model?.state.toString())
        binding.vwAddress.setText(model?.address.toString())
        binding.vwPhone.setText(model?.phone.toString())
        binding.vwEmail.setText(model?.email.toString())

        binding.btnSalir.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}