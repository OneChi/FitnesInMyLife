package ru.vanchikov.fitnesinmylife.util

import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Created on 22.08.2019
 */

// Я сделал функции расширения, чтоб не писать одно и тоже много раз в разных фрагментах.
// удобно будет лаконично писать код для отладки или вывода како-то информации в Toast
fun Fragment.makeToastShort(text: String) = Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

fun Fragment.makeToastLong(text: String) = Toast.makeText(context, text, Toast.LENGTH_LONG).show()