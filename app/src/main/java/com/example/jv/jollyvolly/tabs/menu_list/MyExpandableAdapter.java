package com.example.jv.jollyvolly.tabs.menu_list;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jv.jollyvolly.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by madiyarzhenis on 03.09.15.
 */
public class MyExpandableAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private ArrayList<Object> childtems;
    private LayoutInflater inflater;
    private ArrayList<Parent> parentItems;
    private ArrayList<Child> child;
    ArrayList<String> objectID;

    public static HashMap<String, ArrayList<Child>> listDataChild = new HashMap<>();

    // constructor
    public MyExpandableAdapter(ArrayList<Parent> parents, HashMap<String, ArrayList<Child>> childern, ArrayList<String> objectID) {
        this.parentItems = parents;
        listDataChild = childern;
        this.objectID = objectID;
    }

    public void setInflater(LayoutInflater inflater, Activity activity) {
        this.inflater = inflater;
        this.activity = activity;
    }

    // method getChildView is called automatically for each child view.
    //  Implement this method as per your requirement
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        Log.i("groupPos", objectID.get(groupPosition) + "");
//        child = (ArrayList<Child>) childtems.get(groupPosition);

        child = listDataChild.get(objectID.get(groupPosition));

        TextView textView = null;
        TextView price1, price2, price3, price4;
        final ImageView bucket1, bucket2, bucket3, bucket4;
        TextView textSize1, textSize2, textSize3, textSize4;

        String price[] = child.get(0).getPrice().split(",");
        String size[] = child.get(0).getSize().split(",");
        final String imageUrlBuckets[] = child.get(0).getImageUrl().split(",");

        if (price.length == 2 && size.length == 2 && imageUrlBuckets.length == 2) {
            //the first row is used as header
            if (childPosition == 0) {
                convertView = inflater.inflate(R.layout.child_header_view_2, null);
                ImageView bucket_1 = (ImageView) convertView.findViewById(R.id.imageSize1);
                final ImageView bucket_2 = (ImageView) convertView.findViewById(R.id.imageSize2);
//                ImageView[] imageViews = {bucket1, bucket2};
//                for (int i = 0; i < imageViews.length; i++) {

                Picasso.with(activity)
                        .load(imageUrlBuckets[0])
                        .into(bucket_1, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {

                            }
                        });
                Picasso.with(activity)
                        .load(imageUrlBuckets[1])
                        .into(bucket_2);
//                }

//                textSize1 = (TextView) convertView.findViewById(R.id.textViewSize1);
//                textSize2 = (TextView) convertView.findViewById(R.id.textViewSize2);
//                textSize1.setText(size[0]);
//                textSize2.setText(size[1]);
            } else {
                convertView = inflater.inflate(R.layout.menu_child_view_2, null);
                // get the textView reference and set the value
                textView = (TextView) convertView.findViewById(R.id.textViewChild);
                price1 = (TextView) convertView.findViewById(R.id.textViewPrice1);
                price2 = (TextView) convertView.findViewById(R.id.textViewPrice2);

                textView.setText(child.get(childPosition-1).getName());
                price1.setText(price[0]);
                price2.setText(price[1]);
            }
        } else if (price.length == 3 && size.length == 3 && imageUrlBuckets.length == 3) {
            if (childPosition == 0) {
                convertView = inflater.inflate(R.layout.child_header_view_3, null);
                bucket1 = (ImageView) convertView.findViewById(R.id.imageSize1);
                bucket2 = (ImageView) convertView.findViewById(R.id.imageSize2);
                bucket3 = (ImageView) convertView.findViewById(R.id.imageSize3);
                ImageView[] imageViews = {bucket1, bucket2, bucket3};
                for (int i = 0; i < imageViews.length; i++) {
                    Picasso.with(activity)
                            .load(imageUrlBuckets[i])
                            .into(imageViews[i]);
                }

//                textSize1 = (TextView) convertView.findViewById(R.id.textViewSize1);
//                textSize2 = (TextView) convertView.findViewById(R.id.textViewSize2);
//                textSize3 = (TextView) convertView.findViewById(R.id.textViewSize3);
//                textSize1.setText(size[0]);
//                textSize2.setText(size[1]);
//                textSize3.setText(size[2]);
            } else {
                convertView = inflater.inflate(R.layout.menu_child_view_3, null);
                // get the textView reference and set the value
                textView = (TextView) convertView.findViewById(R.id.textViewChild);
                price1 = (TextView) convertView.findViewById(R.id.textViewPrice1);
                price2 = (TextView) convertView.findViewById(R.id.textViewPrice2);
                price3 = (TextView) convertView.findViewById(R.id.textViewPrice3);

                textView.setText(child.get(childPosition-1).getName());
                price1.setText(price[0]);
                price2.setText(price[1]);
                price3.setText(price[2]);
            }

        } else if (price.length == 4 && size.length == 4 && imageUrlBuckets.length == 4) {
            if (childPosition == 0) {
                convertView = inflater.inflate(R.layout.child_header_view_4, null);
                bucket1 = (ImageView) convertView.findViewById(R.id.imageSize1);
                bucket2 = (ImageView) convertView.findViewById(R.id.imageSize2);
                bucket3 = (ImageView) convertView.findViewById(R.id.imageSize3);
                bucket4 = (ImageView) convertView.findViewById(R.id.imageSize4);
                final ImageView[] imageViews = {bucket1, bucket2, bucket3, bucket4};
                for (int i = 0; i < imageViews.length; i++) {
                    Picasso.with(activity)
                            .load(imageUrlBuckets[i])
                            .into(imageViews[i]);
                }

//                textSize1 = (TextView) convertView.findViewById(R.id.textViewSize1);
//                textSize2 = (TextView) convertView.findViewById(R.id.textViewSize2);
//                textSize3 = (TextView) convertView.findViewById(R.id.textViewSize3);
//                textSize4 = (TextView) convertView.findViewById(R.id.textViewSize4);
//                textSize1.setText(size[0]);
//                textSize2.setText(size[1]);
//                textSize3.setText(size[2]);
//                textSize4.setText(size[3]);
            } else {
                convertView = inflater.inflate(R.layout.menu_child_view_4, null);
                // get the textView reference and set the value
                textView = (TextView) convertView.findViewById(R.id.textViewChild);
                price1 = (TextView) convertView.findViewById(R.id.textViewPrice1);
                price2 = (TextView) convertView.findViewById(R.id.textViewPrice2);
                price3 = (TextView) convertView.findViewById(R.id.textViewPrice3);
                price4 = (TextView) convertView.findViewById(R.id.textViewPrice4);

                textView.setText(child.get(childPosition-1).getName());
                price1.setText(price[0]);
                price2.setText(price[1]);
                price3.setText(price[2]);
                price4.setText(price[3]);
            }
        }
        return convertView;
    }

    // method getGroupView is called automatically for each parent item
    // Implement this method as per your requirement
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        CheckedTextView textView = null;
        ImageView imageView = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.menu_parent_view, null);
        }

//        ((CheckedTextView) convertView).setText(parentItems.get(groupPosition));
//        ((CheckedTextView) convertView).setChecked(isExpanded);

        textView = (CheckedTextView) convertView.findViewById(R.id.textViewGroupName);
//        textView.setChecked(isExpanded);
        textView.setText(parentItems.get(groupPosition).getName());
        imageView = (ImageView) convertView.findViewById(R.id.imageViewMenuParent);

        Picasso.with(activity)
                .load(parentItems.get(groupPosition).getUrl())
                .into(imageView);

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        return ((ArrayList<Child>) childtems.get(groupPosition)).size()+1;
        Log.i("CHILD_COUNT", objectID.get(groupPosition));
        return listDataChild.get(objectID.get(groupPosition)).size()+1;
//        return 4;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}

