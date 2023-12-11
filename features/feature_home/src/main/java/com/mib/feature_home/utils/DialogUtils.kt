package com.mib.feature_home.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mib.feature_home.*
import com.mib.feature_home.interfaces.DialogOrderListener
import com.mib.feature_home.interfaces.ListenerCityList
import com.mib.feature_home.interfaces.ListenerTwoActions
import com.mib.feature_home.adapter.CityAdapter
import com.mib.feature_home.domain.model.City
import com.mib.feature_home.interfaces.GiveRatingListener
import com.mib.feature_home.utils.utils_interface.DatePickerListener
import com.mib.lib_navigation.DialogListener
import com.mib.feature_home.utils.utils_interface.DialogOneButtonListener
import com.mib.feature_home.utils.utils_interface.TimeDialogListener

class DialogUtils {
    companion object {
        fun showDialogGpsOff(context: Context, dialogListenerTwoAction: ListenerTwoActions) {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Your GPS is off")
                .setIcon(R.drawable.ic_icon)
                .setMessage("Please turn on to continue")
                .setNegativeButton("Later") { dialogInterface, _ ->
                    dialogInterface.cancel()
                    dialogListenerTwoAction.firstAction()
                }
                .setPositiveButton("Turn on") { dialogInterface, _ ->
                    dialogInterface.cancel()
                    dialogListenerTwoAction.secondAction()
                }
        }

        fun showBottomDialogActivateLocationThroughSettings(context: Context, dialogListenerTwoAction: ListenerTwoActions) {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Turn on your GPS")
                .setIcon(R.drawable.ic_icon)
                .setMessage("Let's turn it on")
                .setNegativeButton("Later") { dialogInterface, _ ->
                    dialogInterface.cancel()
                    dialogListenerTwoAction.firstAction()
                }
                .setPositiveButton("Turn on") { dialogInterface, _ ->
                    dialogInterface.cancel()
                    dialogListenerTwoAction.secondAction()
                }
        }

        fun showDialogList(context: Context, cityList: List<City>?, listener: ListenerCityList) {
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layoutView: View = inflater.inflate(R.layout.dialog_list, null)
            dialogBuilder.setView(layoutView)
            val alertDialog = dialogBuilder.create()

            val rvList = layoutView.findViewById<RecyclerView>(R.id.rvLocation)
            rvList.layoutManager = LinearLayoutManager(context)
            val adapter = CityAdapter(cityList.orEmpty(), object : CityAdapter.OnItemClickListener {
                override fun onClick(city: City) {
                    listener.action(city)
                    alertDialog.dismiss()
                }
            })
            rvList.adapter = adapter

            alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            alertDialog.show()
        }

        fun showDialogImage(context: Context, image: String?) {
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layoutView: View = inflater.inflate(R.layout.dialog_image, null)
            val ivImage = layoutView.findViewById<ImageView>(R.id.ivImage)
            val btClose = layoutView.findViewById<Button>(R.id.btClose)
            Glide.with(context).load(image).into(ivImage)
            dialogBuilder.setView(layoutView)
            val alertDialog = dialogBuilder.create()
            alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()

            btClose.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        fun showDialogWithTwoButtons(
            context: Context?,
            title: String,
            subtitle: String,
            left: String,
            right: String,
            dialogListener: DialogListener,
        ) {
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layoutView: View = inflater.inflate(R.layout.dialog_two_buttons, null)
            val tvTitle = layoutView.findViewById<TextView>(R.id.tvTitle)
            val tvSubTitle = layoutView.findViewById<TextView>(R.id.tvSubTitle)
            val btLeft = layoutView.findViewById<Button>(R.id.btLeft)
            val btRight = layoutView.findViewById<Button>(R.id.btRight)

            tvTitle.text = title
            tvSubTitle.text = subtitle
            btLeft.text = left
            btRight.text = right
            dialogBuilder.setView(layoutView)
            val alertDialog = dialogBuilder.create()
            alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()
            btLeft.setOnClickListener {
                alertDialog.dismiss()
                dialogListener.onLeftButtonClicked()
            }
            btRight.setOnClickListener {
                alertDialog.dismiss()
                dialogListener.onRightButtonClicked()
            }
        }

        fun showDialogWithOneButton(
            context: Context?,
            title: String,
            subtitle: String,
            buttonText: String,
            dialogListener: DialogOneButtonListener
        ) {
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layoutView: View = inflater.inflate(R.layout.dialog_one_button, null)
            val tvTitle = layoutView.findViewById<TextView>(R.id.tvTitle)
            val tvSubTitle = layoutView.findViewById<TextView>(R.id.tvSubTitle)
            val btOk = layoutView.findViewById<Button>(R.id.btOk)

            tvTitle.text = title
            tvSubTitle.text = subtitle
            btOk.text = buttonText
            dialogBuilder.setView(layoutView)
            val alertDialog = dialogBuilder.create()
            alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()
            btOk.setOnClickListener {
                alertDialog.dismiss()
                dialogListener.onSubmitClicked()
            }
        }

        fun showOrderConfirmationDialog(
            context: Context?,
            dialogListener: DialogOrderListener
        ) {
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layoutView: View = inflater.inflate(R.layout.dialog_order_confirmation, null)
            val etBookingDate = layoutView.findViewById<EditText>(R.id.etBookingDate)
            val etBookingTime = layoutView.findViewById<EditText>(R.id.etBookingTime)
            val etBookingAddress = layoutView.findViewById<EditText>(R.id.etBookingAddress)
            val etBookingNotes = layoutView.findViewById<EditText>(R.id.etBookingNotes)
            val btCancel = layoutView.findViewById<Button>(R.id.btCancel)
            val btOrder = layoutView.findViewById<Button>(R.id.btOrder)

            etBookingDate.let { et ->
                et.setOnClickListener {
                    et.openDatePicker(context, object: DatePickerListener {
                        override fun onFinishSelectDate(result: String) {
                            etBookingDate.setText(result)
                        }
                    })
                }
            }

            etBookingTime.let { et ->
                et.setOnClickListener {
                    et.openTimePicker(context, object: TimeDialogListener {
                        override fun onFinishSelectTime(result: String) {
                            etBookingTime.setText(result)
                        }
                    })
                }
            }

            dialogBuilder.setView(layoutView)
            val alertDialog = dialogBuilder.create()
            alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()
            btCancel.setOnClickListener {
                alertDialog.dismiss()
            }
            btOrder.setOnClickListener {
                alertDialog.dismiss()
                dialogListener.order(etBookingDate.text.toString(), etBookingTime.text.toString(), etBookingAddress.text.toString(), etBookingNotes.text.toString())
            }
        }

        fun showGiveReviewDialog(
            context: Context?,
            dialogListener: GiveRatingListener
        ) {
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layoutView: View = inflater.inflate(R.layout.dialog_give_rating, null)
            val ratingBar = layoutView.findViewById<RatingBar>(R.id.ratingBar)
            val etReview = layoutView.findViewById<EditText>(R.id.etReview)
            val btCancel = layoutView.findViewById<Button>(R.id.btCancel)
            val btSend = layoutView.findViewById<Button>(R.id.btSend)

            dialogBuilder.setView(layoutView)
            val alertDialog = dialogBuilder.create()
            alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()
            btCancel.setOnClickListener {
                alertDialog.dismiss()
            }
            btSend.setOnClickListener {
                alertDialog.dismiss()
                dialogListener.sendRating(
                    ratingBar.rating.toInt().toString(),
                    etReview.text.toString()
                )
            }
        }
    }
}