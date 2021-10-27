package com.example.appejemplosql

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.content.ContentValues
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var txtId : EditText
    lateinit var txtDescripcion : EditText
    lateinit var txtPrecio : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtId = findViewById(R.id.txtId)
        txtDescripcion = findViewById(R.id.txtNombre)
        txtPrecio = findViewById(R.id.txtPrecio)

        val btnRegistrar = findViewById<Button>(R.id.buttonRegistrar)
        val btnConsultar = findViewById<Button>(R.id.buttonConsultar)
        val btnActualizar = findViewById<Button>(R.id.buttonActualizar)
        val btnEliminar = findViewById<Button>(R.id.buttonEliminar)

        btnRegistrar.setOnClickListener { registrar() }

        btnEliminar.setOnClickListener { eliminar() }

        btnConsultar.setOnClickListener {
                when {
                    !txtId.text.toString().equals("") -> {
                        consultarPorId()
                    }
                    !txtDescripcion.text.toString().equals("") && txtId.text.toString().equals("") -> {
                        consultarPorDescripcion()
                    }
                    else -> {
                        Toast.makeText(this, "Escriba el Id o nombre", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        btnActualizar.setOnClickListener { actualizar() }
    }

    fun validarCamposVacios(): Boolean {
        return !txtId.text.toString().equals("") &&
                !txtDescripcion.text.toString().equals("")
                !txtPrecio.text.toString().equals("")
    }
    
    fun registrar() {
        if(validarCamposVacios()) {
                val admin = AdminSQLite(this, "administracion", null, 1)
                val bd = admin.writableDatabase
                val registro = ContentValues()
                registro.put("codigo", txtId.getText().toString())
                registro.put("descripcion", txtDescripcion.getText().toString())
                registro.put("precio", txtPrecio.getText().toString())

                bd.insert("articulos", null, registro)
                bd.close()

                limpiarCampos()

                Toast.makeText(this, "Fue registrado correctamente", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Hay campos vacíos", Toast.LENGTH_SHORT).show()
        }
    }

    fun eliminar() {
        if (!txtId.text.toString().equals("")) {
                val admin = AdminSQLite (this, "administracion", null, 1)
                val bd = admin.writableDatabase
                val cantidad = bd.delete("articulos", "codigo = '${txtId.text.toString()}'", null)

                bd.close()

                limpiarCampos()

                if (cantidad == 1)
                    Toast.makeText(this, "Eliminacion exitosa del prooducto", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "No se encontró el articulo", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Por favor escriba el Id del producto", Toast.LENGTH_SHORT).show()
        }
    }

    fun actualizar() {
        if (validarCamposVacios()) {
                val admin = AdminSQLite (this, "administracion", null, 1)
                val bd = admin.writableDatabase
                val registro = ContentValues()

                registro.put("descripcion", txtDescripcion.text.toString())
                registro.put("precio", txtPrecio.text.toString())
                val cantidad = bd.update("articulos", registro, "codigo = '${txtId.text.toString()}'", null)

                bd.close()

                limpiarCampos()

                if (cantidad == 1)
                    Toast.makeText(this, "Registro actualizado", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "No se encontró el articulo", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Existen campos vacíos", Toast.LENGTH_SHORT).show()
        }
    }

    fun consultarPorDescripcion() {
        val admin = AdminSQLite (this, "administracion", null, 1)
        val bd = admin.writableDatabase
        val fila = bd.rawQuery("SELECT codigo, precio FROM articulos WHERE descripcion = '${txtDescripcion.text.toString()}'", null)

        if (fila.moveToFirst()) {
            txtId.setText(fila.getString(0))
            txtPrecio.setText(fila.getString(1))
        } else {
            Toast.makeText(this, "No existe el artículo", Toast.LENGTH_LONG).show()
        }

        bd.close()
    }

    fun consultarPorId() {
        val admin = AdminSQLite (this, "administracion", null, 1)
        val bd = admin.writableDatabase
        val fila = bd.rawQuery("SELECT descripcion, precio FROM articulos WHERE codigo = '${txtId.text.toString()}'", null)

        if (fila.moveToFirst()) {
            txtDescripcion.setText(fila.getString(0))
            txtPrecio.setText(fila.getString(1))
        } else {
            Toast.makeText(this, "No existe el artículo", Toast.LENGTH_LONG).show()
        }

        bd.close()
    }

    fun limpiarCampos() {
        txtId.setText("")
        txtDescripcion.setText("")
        txtPrecio.setText("")
    }

}