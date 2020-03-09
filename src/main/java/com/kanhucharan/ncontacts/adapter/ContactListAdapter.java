package com.kanhucharan.ncontacts.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.kanhucharan.ncontacts.MainActivity;
import com.kanhucharan.ncontacts.R;
import com.kanhucharan.ncontacts.database.SqlHandler;
import com.kanhucharan.ncontacts.entityset.ContactsEntityset;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactListAdapter extends BaseAdapter {

    Context context;
    ArrayList<ContactsEntityset> contactList;
    ContactsEntityset contactListItems;
    SqlHandler sqlHandler;
    private int lastPosition = -1;
    public ContactListAdapter(Context context, ArrayList<ContactsEntityset> list) {

        this.context = context;
        contactList = list;
    }

    @Override
    public int getCount() {

        return contactList.size();
    }

    @Override
    public Object getItem(int position) {

        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup arg2) {
        contactListItems = contactList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.show_list_items, null);

        }
        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.down : R.anim.anmi);
        animation.setDuration(1000);
        convertView.startAnimation(animation);

        lastPosition = position;
        CircleImageView circleImageView = convertView.findViewById(R.id.profile_image);
        Picasso.with(context).load(contactListItems.getImages()).fit().centerCrop()
                .placeholder(R.drawable.ic_contact_phone_black_24dp)
                .into(circleImageView);

        final TextView id = (TextView) convertView.findViewById(R.id.tv_id);
        id.setText(contactListItems.getSlno());
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(contactListItems.getName());
        TextView tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
        tvPhone.setText(contactListItems.getPhone());
        ImageButton edit = convertView.findViewById(R.id.edit);
        ImageButton delect = convertView.findViewById(R.id.delect);

        // Edit the contacts
        final View finalConvertView = convertView;
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowEditDialog(finalConvertView, position);
            }
        });
        // Delect Contects and list items
        delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure to delete!")
                        .setCancelText("No")
                        .showCancelButton(true)
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog
                                        .setTitleText("Deleted!!")
                                        .setContentText("Your Request has been Deleted!")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(null)
                                        .showCancelButton(false)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                // delect the contact
								/*contactListItems.remove(position);
								notifyDataSetChanged();*/
                                contactList.remove(position);
                                notifyDataSetChanged();
                                sqlHandler = new SqlHandler(context);
                                sqlHandler.delete(id.getText().toString().trim());
                                if(contactList.size()==0)
                                {
                                    MainActivity.btnsave.setText("SAVE");
                                }


                            }
                        })
                        .show();
            }

        });
        return convertView;
    }

    private void ShowEditDialog(final View convertView, int position) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = convertView.findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.edit_show_popup, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        contactListItems = contactList.get(position);
        Button declineButton = (Button) alertDialog.findViewById(R.id.buttonOk);
        final CircleImageView chngimage = alertDialog.findViewById(R.id.chng_img);
        // chngimage.setImageURI(Uri.parse(contactListItems.getImages()));
        Picasso.with(context).load(contactListItems.getImages()).fit().centerCrop()
                .placeholder(R.drawable.ic_contact_phone_black_24dp)
                .into(chngimage);
        final EditText chngname = alertDialog.findViewById(R.id.chng_name);
        chngname.setText(contactListItems.getName());
        final EditText chngphone = alertDialog.findViewById(R.id.chng_phone);
        chngphone.setText(contactListItems.getPhone());
        final TextView id = alertDialog.findViewById(R.id.tt);
        id.setText(contactListItems.getSlno());
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(chngname.getText().toString()) || TextUtils.isEmpty(chngphone.getText().toString())) {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("!!!!Empty field!!!")
                            .show();
                } else {
                    sqlHandler = new SqlHandler(context);
                    String name = chngname.getText().toString();
                    String phone = chngphone.getText().toString();
                    String id1 = id.getText().toString();
                    sqlHandler.update(id1, name, phone);
                    alertDialog.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("success")
                            .setContentText("Your Request has been updated!")
                            .show();
                }

            }
        });

    }

}
