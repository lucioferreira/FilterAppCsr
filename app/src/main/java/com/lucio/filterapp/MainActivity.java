package com.lucio.filterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<String> mItems;
    EditText editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editSearch = (EditText) findViewById(R.id.edit_search);

        mItems = new ArrayList<>();
        mItems = fillInitialItems();

        RecyclerView recyclerView = findViewById(R.id.recyclerview_list);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchAdapter(this, mItems);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
    }

    public void onClickSearch(View view) {
        String search = editSearch.getText().toString();
        if(search.isEmpty()){
            ArrayList<String> list = fillInitialItems();
            mItems.clear();
            mItems.addAll(list);
            mAdapter.notifyDataSetChanged();
            return;
        }

        ArrayList<String> list = getFilteredList(search);
        mItems.clear();
        mItems.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    private ArrayList<String> getFilteredList(String word) {

        ArrayList<String> list = new ArrayList<String>();
        for (String item : mItems){
            boolean hasTypo = checkTypo(word, item);
            boolean hasJumbled = checkJumbled(word, item);
            if(hasTypo && hasJumbled) {
                continue;
            }
            if(checkTypo(word, item) || checkJumbled(word, item)){
                list.add(item);
            }
        }
        return list;
    }

    private boolean checkJumbled(String first, String second){
        if(second.length() == first.length()) {
            if(first.charAt(0) == second.charAt(0)){
                int wordLen = second.length();
                int charDiffCounter = 0;
                for(int i = 0; i < wordLen; i++){
                    if(second.charAt(i) != first.charAt(i)){
                        charDiffCounter = charDiffCounter + 1;
                    }
                }

                if(wordLen > 3){
                    Double calc = wordLen * (2.0/3.0);
                    if(charDiffCounter < calc){
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return (charDiffCounter > 0);
                }
            } else {
                return false;
            }
        }

        return false;
    }

    private boolean checkTypo(String first, String second){
        int lenFirst = first.length();
        int lenSecond = second.length();

        if(Math.abs(lenFirst-lenSecond)>1){
            return false;
        }

        int counter = 0, indexA = 0, indexB = 0;

        while(indexA < lenFirst && indexB < lenSecond) {
            if(first.charAt(indexA) != second.charAt(indexB)){
                if(counter == 1){
                    return false;
                }
                if(lenFirst > lenSecond){
                    indexA++;
                } else if (lenFirst < lenSecond){
                    indexB++;
                } else {
                    indexA++; indexB++;
                }
                counter = counter + 1;
            } else {
                indexA++; indexB++;
            }
        }
        if(indexA < lenFirst || indexB < lenSecond) {
            counter = counter + 1;
        }

        return (counter == 1);
    }

    private ArrayList<String> fillInitialItems() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("ple");
        list.add("pale");
        list.add("bale");
        list.add("bake");
        return list;
    }

}
