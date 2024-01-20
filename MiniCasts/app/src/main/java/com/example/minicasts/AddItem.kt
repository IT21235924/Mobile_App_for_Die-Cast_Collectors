package com.example.minicasts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.minicasts.databinding.ActivityAddItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddItem : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val items = listOf("Hot Wheels","Takara Tomy","MiniAuto","Miniso","Majorette","MatchBox")

        val autoComplete : AutoCompleteTextView = findViewById(R.id.auto_complete)

        val adapter = ArrayAdapter(this,R.layout.activity_list_items,items)

        autoComplete.setAdapter(adapter)

        autoComplete.onItemClickListener = AdapterView.OnItemClickListener{
            adapterView, view, i, l ->

            val itemSelected = adapterView.getItemAtPosition(i)
            Toast.makeText(this,"Item: $itemSelected",Toast.LENGTH_SHORT).show()

        }

        binding.addItemBtn.setOnClickListener {

            val name = binding.inputName.text.toString()
            val vManu = binding.inputVManu.text.toString()
            val year = binding.inputYear.text.toString()
            val color = binding.inputColor.text.toString()
            val special = binding.inputSpecial.text.toString()

            if (name.isNotEmpty() && vManu.isNotEmpty() && year.isNotEmpty() && color.isNotEmpty() && special.isNotEmpty()){

                val userID = FirebaseAuth.getInstance().currentUser!!.uid

                val userMap = hashMapOf(
                    "Car_Name" to name,
                    "Vehicle_Manufacturer" to vManu,
                    "Year" to year,
                    "Color" to color,
                    "Special_Details" to special
                )

                db.collection("Cars").document(userID).set(userMap).addOnSuccessListener {
                    Toast.makeText(this, "Successfully Added!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, UserDashboard::class.java)
                    startActivity(intent)

                }.addOnFailureListener {
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            }

        }

    }
}