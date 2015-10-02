package com.example.jv.jollyvolly.tabs.menu_list;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.jv.jollyvolly.R;
import com.google.gson.Gson;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by madiyarzhenis on 31.08.15.
 */
public class MenuList extends Fragment {

    ExpandableListView expandableListView;
    private ArrayList<Parent> parentItems = new ArrayList<Parent>();
    private ArrayList<Object> childItems = new ArrayList<Object>();
    ArrayList<Child> child;
    Gson gson;
    HashMap<String, Object> mapList;
    HashMap<String, Object> subMenuList;
    MyExpandableAdapter adapter;
    ArrayList<String> objectID;

    HashMap<String, ArrayList<Child>> listDataChild;
    ArrayList<Parent> listDataHeader;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_list_fragment, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        expandableListView.setDividerHeight(2);
//        expandableListView.setGroupIndicator(null);
        expandableListView.setClickable(true);

        objectID = new ArrayList<>();
        mapList = new HashMap<String, Object>();
        gson = new Gson();

        listDataHeader = new ArrayList<Parent>();
        listDataChild = new HashMap<String, ArrayList<Child>>();

        ParseCloud.callFunctionInBackground("menu_list", mapList, new FunctionCallback<Object>() {
            public void done(Object response, ParseException e) {
                if (e != null) {

                } else {
                    parentItems.clear();
                    childItems.clear();
                    String json = gson.toJson(response);
                    if (json.equals("[]")) {
                        Toast.makeText(getActivity(), "menu NULL", Toast.LENGTH_LONG).show();
                    } else {
                        Log.i("JSON_MENU", json);
                        try {
                            JSONArray jsonArray = new JSONArray(json);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONObject estimatedData = jsonObject.getJSONObject("estimatedData");
                                JSONObject jsonImage = estimatedData.getJSONObject("image");

                                String imageUrl = jsonImage.getString("url");
                                String name = estimatedData.getString("name");
                                final String objectId = jsonObject.getString("objectId");
                                listDataHeader.add(new Parent(name, imageUrl));
                                Log.i("ObjectId_", objectId);
                                objectID.add(objectId);
                                subMenuList = new HashMap<String, Object>();
                                subMenuList.put("menu_id", objectId);
                                ParseCloud.callFunctionInBackground("sub_menu_list", subMenuList, new FunctionCallback<Object>() {

                                    public void done(Object response, ParseException e) {
                                        if (e != null) {

                                        } else {
                                            Log.i("InSubMenuList", subMenuList.get("menu_id") + "");
                                            String json = gson.toJson(response);
                                            if (json.equals("[]")) {
                                                Toast.makeText(getActivity(), "menu NULL", Toast.LENGTH_LONG).show();
                                            } else {
                                                child = new ArrayList<Child>();
                                                String menuObjectId = null;
                                                try {
                                                    Log.i("JSON_sm", json);
                                                    JSONArray jsonArray = new JSONArray(json);
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                        JSONObject estimatedData = jsonObject.getJSONObject("estimatedData");
                                                        JSONObject menuObject = estimatedData.getJSONObject("menu_id");

                                                        menuObjectId = menuObject.getString("objectId");
                                                        String name = estimatedData.getString("name");
                                                        String objectId = jsonObject.getString("objectId");
                                                        String imageUrl = estimatedData.getString("image");
                                                        String price = estimatedData.getString("price");
                                                        String size = estimatedData.getString("size");

                                                        Log.i("Name,image,price,size", name + "," + imageUrl + "," + price + "," + size);
                                                        //setChild
                                                        child.add(new Child(name, imageUrl, price, size));
                                                        //setChildData(name, imageUrl, price, size);
                                                    }
//                                                    childItems.add(child);
                                                    listDataChild.put(menuObjectId, child);
                                                    adapter.notifyDataSetChanged();

//                                    adapter.notifyDataSetChanged();

                                                    // Create the Adapter
//                                    adapter = new MyExpandableAdapter(parentItems, childItems);
//                                    adapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), getActivity());
//
//                                    // Set the Adapter to expandableList
//                                    expandableListView.setAdapter(adapter);
                                                } catch (JSONException e1) {
                                                    e1.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                });
//                                setGroupParents(name, imageUrl);
//                                objectID.add(objectId);
                                Log.i("count", i + "");
                            }
//                            setChildData();
                            // Create the Adapter
                            adapter = new MyExpandableAdapter(listDataHeader, listDataChild, objectID);
                            adapter.setInflater((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE), getActivity());

                            // Set the Adapter to expandableList
                            expandableListView.setAdapter(adapter);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
//                        Toast.makeText(getApplicationContext(), "banner not NULL", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        // Set the Items of Parent
        // Set The Child Data
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {
                Toast.makeText(getActivity(), "Clicked" + groupPosition, Toast.LENGTH_LONG).show();
//                if (parent.isGroupExpanded(groupPosition)) {
//                    parent.collapseGroup(groupPosition);
//                }else {
//                    parent.expandGroup(groupPosition);
//                }
//                if (MyExpandableAdapter.listDataChild.get(objectID.get(groupPosition)) == null) {
//                if (adapter.getChildrenCount(groupPosition) == 0) {


//                if (MyExpandableAdapter.listDataChild.get(objectID.get(groupPosition)) == null) {
//                    Log.i("NULLLLLLL", "NULLLLLLL");
//
//                }
//                }
                return false;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getActivity(), "Clicked" + childPosition, Toast.LENGTH_LONG).show();
                return false;
            }
        });
        return view;
    }

    public void setGroupParents(String name, String imageUrl) {
        parentItems.add(new Parent(name, imageUrl));
    }

    // method to set child data of each parent
    public void setChildData(String name, String imageUrl, String price, String size) {

        Log.i("Name,image,price,size", name + "," + imageUrl + "," + price + "," + size);
        // Add Child Items for Fruits
        child.add(new Child(name, imageUrl, price, size));
        childItems.add(child);
        Log.i("SIZE_CHILD", childItems.size() + "");
    }
}
