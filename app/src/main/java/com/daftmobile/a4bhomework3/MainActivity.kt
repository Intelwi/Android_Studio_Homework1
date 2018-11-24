package com.daftmobile.a4bhomework3

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val REQUEST_SELECT_CONTACT = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EMAIL_RETRIEVER = EmailRetriever.Impl(applicationContext)
        sendMailButton.setOnClickListener(this::createIntent)
    }


    companion object {
        lateinit var EMAIL_RETRIEVER: EmailRetriever
    }

    private fun createIntent(view: View) {
            // Build the intent
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = ContactsContract.Contacts.CONTENT_TYPE
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_CONTACT)
        }
    }

    //do pobrania maila
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            if(data?.data != null) {
                val contactUri: Uri = data.data
                val email = EMAIL_RETRIEVER.retrieve(contactUri)
                val emails = Array<String>(1) { email.toString() }
                this.createEmail(
                        emails,
                        "Wiadomość z pracy domowej"
                )
            }
        }
    }

    //do aplikacji z mailami
    private fun createEmail(addresses: Array<String>, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }


}
