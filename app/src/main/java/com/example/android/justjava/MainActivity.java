package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends ActionBarActivity {

    int quantity = 1;
    int costOfCup = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        EditText editName = (EditText) findViewById(R.id.editName);
        String customName = editName.getText().toString();
        CheckBox checkWhipped = (CheckBox) findViewById(R.id.whippedCheck);
        CheckBox checkChocolate = (CheckBox) findViewById(R.id.chocolateCheck);
        boolean whipped = checkWhipped.isChecked();
        boolean chocolate = checkChocolate.isChecked();
        int price = calculatePrice(whipped, chocolate);
        createOrderSummary(customName, price, whipped, chocolate);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method increments the quantity value on the screen when the plus button is clicked
     */
    public void increment(View view) {
        //Let the quantities of the coffee go up as long as the number hasn't hit 100
        if(quantity < 100) {
            quantity++;
            displayQuantity(quantity);
        }

        //Print toast telling the user they can't order more than 100 coffees
        if(quantity == 100) {
            Toast.makeText(MainActivity.this, R.string.text_minOrder,
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * This method decrements the quantity value on the screen when the minus button is clicked
     */
    public void decrement(View view) {
        //Let the user lower the quantity all the way down to 1
        if(quantity > 1) {
            quantity--;
            displayQuantity(quantity);
        }

        if(quantity == 1) {
            //Print toast telling the user they must order at least one coffee
            Toast.makeText(MainActivity.this, R.string.text_maxOrder,
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * Calculates the price of the order based on the current quantity.
     *
     * @return the total price
     */
    private int calculatePrice(boolean whipped, boolean chocolate) {
        costOfCup = 5;
        if(whipped) {costOfCup += 1;}
        if(chocolate) {costOfCup += 2;}

        int price = quantity * costOfCup;
        return price;
    }

    /**
     * Creates a summary of the order including the customer's name, quantity ordered, and price.
     * @param price for the order
     * @return summary message
     */
    private String createOrderSummary(String name, int price, boolean whippedCheck, boolean chocolateCheck) {
        String orderMessage = getString(R.string.email_name) + name + "\n";

        if(whippedCheck) {orderMessage += getString(R.string.email_whippedY);}
        else {orderMessage += getString(R.string.email_whippedN);}

        if(chocolateCheck) {orderMessage += getString(R.string.email_chocY);}
        else {orderMessage += getString(R.string.email_chocN);}

        orderMessage += getString(R.string.email_quantity) + quantity + "\n";
        orderMessage += getString(R.string.email_total) + price + "\n";
        orderMessage += getString(R.string.email_thankYou);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.text_orderSub) + name);
        intent.putExtra(Intent.EXTRA_TEXT, orderMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        return orderMessage;
    }
}