package com.example.tipcalculator

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.inflate
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import kotlinx.android.synthetic.main.activity_main.*

private const val INITIAL_TIP_PERCENT = 15
private const val DEFAULT_TOTAL_AMOUNT = 0.00
private const val DEFAULT_TIP_AMOUNT = 0.00
private const val DEFAULT_SPLIT_AMOUNT = 0.00
private const val INITIAL_TIP_VALUE = 2
private const val DEFAULT_SPLIT_VALUE_AT_ZERO_POSITION = 1

class MainActivity : AppCompatActivity() {
    private var totalAmount: Double = 0.0
    private var minimumValue: Int = 0

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercent.text = "$INITIAL_TIP_PERCENT%"
        tvSplitSeekBar.text = "SPLIT BY $INITIAL_TIP_VALUE"
        updateTipDescription(INITIAL_TIP_PERCENT)
        tvTotalAmount.text = DEFAULT_TOTAL_AMOUNT.toString()
        tvTipAmount.text = DEFAULT_TIP_AMOUNT.toString()
        tvSplitResult.text = DEFAULT_SPLIT_AMOUNT.toString()

        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                tvTipPercent.text = "$progress%"
                updateTipDescription(progress)
                computeTipAndTotal()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        splitSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
          // var progressChanged = splitSeekBar
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                tvSplitSeekBar.text = "SPLIT BY $progress"
                computeSplitResult()
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

       // switch_button.setOnClickListener(View.OnClickListener {

       // })

    }

      fun computeSplitResult() {
          if (etBase.text.isEmpty()){
              tvSplitResult.text = DEFAULT_SPLIT_AMOUNT.toString()
              return
          }
          var mySplit = splitSeekBar.progress
          if (mySplit == minimumValue){
              tvSplitResult.text = DEFAULT_SPLIT_VALUE_AT_ZERO_POSITION.toString()
              mySplit = DEFAULT_SPLIT_VALUE_AT_ZERO_POSITION
              val totalSplitResult = totalAmount / mySplit
              tvSplitResult.text = "%.2f".format(totalSplitResult)
          }
          val totalSplitResult = totalAmount / mySplit
          tvSplitResult.text = "%.2f".format(totalSplitResult)
    }

    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription: String
        when(tipPercent){
          in 0..9 -> tipDescription = "Poor"
            in 9..14 -> tipDescription = "Acceptable"
            in 14..19 ->tipDescription = "Good"
            in 19..24 ->tipDescription = "Great"
            else -> tipDescription = "Amazing"
        }
        tvTipDescription.text = tipDescription
        val color = ArgbEvaluator().evaluate(tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this,R.color.worstTip), ContextCompat.getColor(this, R.color.bestTip)
        )as Int

        tvTipDescription.setTextColor(color)
    }

     private fun computeTipAndTotal() {
        if (etBase.text.isEmpty()){
            tvTipAmount.text = DEFAULT_TIP_AMOUNT.toString()
            tvTotalAmount.text = DEFAULT_TOTAL_AMOUNT.toString()
            return
        }

        val baseAmount = etBase.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        val tipAmount  = baseAmount * tipPercent / 100
         totalAmount = baseAmount + tipAmount

        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)

     }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.dark_mode_switch ->
               // Toast.makeText(this, "Dark mode activated", Toast.LENGTH_SHORT).show()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        }
        return super.onOptionsItemSelected(item)
    }
}