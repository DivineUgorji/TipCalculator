package com.example.tipcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tipPercent.text = "$INITIAL_TIP_PERCENT%"

        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                tipPercent.text = "$progress"
                computeTipAndTotal()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        etBase.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                computeTipAndTotal()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun computeTipAndTotal() {
        if (etBase.text.isEmpty()){
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }
        val baseAmount = etBase.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        val tipAmount  = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipPercent

        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)

    }
}