package com.example.afternoonsqlitedbapp

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var edttext:EditText
    lateinit var edtemail:EditText
    lateinit var edtid:EditText
    lateinit var btnsave:Button
    lateinit var btnview:Button
    lateinit var btndelete:Button
    lateinit var db:SQLiteDatabase
    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edttext  = findViewById(R.id.edttext)
        edtemail = findViewById(R.id.edtemail)
        edtid = findViewById(R.id.edtid)
        btnsave = findViewById(R.id.btnsave)
        btnview = findViewById(R.id.btnview)
        btndelete = findViewById(R.id.btndelete)
        //create database
        db = openOrCreateDatabase("valedictoriandb", Context.MODE_PRIVATE,null)
        //create a table inside the database
        db.execSQL("CREATE TABLE IF NOT EXISTS users(name VARCHAR, email VARCHAR, id_number VARCHAR)")
        btnsave.setOnClickListener {
            //receive data from the user
            val name = edttext.text.toString()
            val email = edtemail.text.toString()
            val idnumber = edtid.text.toString()
            //check if the user is submitting empty fields
            if(name.isEmpty()){
                edttext.setError("Please fill this input")
                edttext.requestFocus()
            } else if(email.isEmpty()){
                edtemail.error = "Please fill this input"
                edtemail.requestFocus()
            }else if(idnumber.isEmpty()){
                edtid.error = "Please fill this field"
                edtid.requestFocus()
            }else{
                //proceed to save the data
                db.execSQL("INSERT INTO users VALUE($name, $email, $idnumber)")
                Toast.makeText(this, "Data sent successfully", Toast.LENGTH_SHORT).show()
                edttext.text = null
                edtemail.text = null
                edtid.setText(null)
            }
        }
        btnview.setOnClickListener {
            //Use cursor to select all the users
            val cursor =db.rawQuery("SELECT* FROM users", null)
            //Check if there is any record in the db

            if (cursor.count==0){
                displayUser("NO RECORDS", "Sorry no data")
            }else{
                //Use a string buffer to append records fron db
                val buffer = StringBuffer()
                while (cursor.moveToNext()){
                    val retrievedName = cursor.getString(0)
                    val retrievedEmail = cursor.getString(1)
                    val retrievedIdNumber= cursor.getString(2)
                    buffer.append(retrievedName+"\n")
                    buffer.append(retrievedEmail+"\n")
                    buffer.append(retrievedIdNumber+"\n")
                }
                displayUser("USERS", buffer.toString())
            }
        }
        btndelete.setOnClickListener {
            //Receive the ID of the user to be deleted
            val idNumber = edtid.text.toString()
            //Check if idNumber received is empty
            if(idNumber.isEmpty()){
                edtid.error = "Please fill this input"
                edtid.requestFocus()
            }else{
                //proceed to delete
                //use cursor to select the user with the id
                val cursor = db.rawQuery("SELECT*FROM users WHERE id_number=$idNumber",null)
                //Check if the user with provided ID exists
                if (cursor.count==0){
                    displayUser("NO USER", "Sorry,no data")
                }else{
                    //Delete the user
                    db.execSQL("DELETE FROM users WHERE id_number=$idNumber")
                    displayUser("SUCCESS","User deleted! ")
                    edtid.text = null
                }
            }
        }
        }
      fun displayUser(title:String, message:String){
         val alertDialog = AlertDialog.Builder(this,)
         alertDialog.setTitle(title)
         alertDialog.setMessage(message)
         alertDialog.setPositiveButton("Close", null)
         alertDialog.create().show()
      }

    }





