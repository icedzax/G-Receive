package com.zubb.jannarongj.zzubbreceive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jannarong.j on 10/19/2018.
 */

public class SearchAdapter extends BaseAdapter implements Filterable {

    Context c ;
    ArrayList<Vbeln> vbelns;
    CustomFilter izefilter;
    ArrayList<Vbeln> filterList;

    public SearchAdapter(Context ctx,ArrayList<Vbeln> vbelns){
        this.c=ctx;
        this.vbelns = vbelns;
        this.filterList = vbelns;
    }

    @Override
    public int getCount() {
        return vbelns.size();
    }

    @Override
    public Object getItem(int pos) {
        return vbelns.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return vbelns.indexOf(getItem(pos));
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView==null){
            convertView=inflater.inflate(R.layout.adp_listitem,null);
        }

        TextView txt_vbeln = (TextView) convertView.findViewById(R.id.vbeln);
        TextView txt_arktx = (TextView) convertView.findViewById(R.id.arktx);
        TextView txt_carlicense = (TextView) convertView.findViewById(R.id.carlicense);
        TextView txt_posnr = (TextView) convertView.findViewById(R.id.posnr);

        txt_vbeln.setText(vbelns.get(pos).getVbeln_name());
        txt_arktx.setText(vbelns.get(pos).getArktx());
        txt_carlicense.setText(vbelns.get(pos).getCarlicense());
        txt_posnr.setText(vbelns.get(pos).getPosnr());


        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(izefilter == null){
            izefilter=new CustomFilter();
        }
        return izefilter;
    }

    private class CustomFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults  results = new  FilterResults();
            if(constraint != null && constraint.length()>0){

                constraint=constraint.toString().toUpperCase();

                ArrayList<Vbeln> filters = new ArrayList<Vbeln>();

                for(int i=0;i<filterList.size();i++){
                    if(filterList.get(i).getVbeln_name().toUpperCase().contains(constraint)){
                        Vbeln v = new Vbeln(filterList.get(i).getVbeln_name(),filterList.get(i).getArktx(),filterList.get(i).getCarlicense(),filterList.get(i).getPosnr());
                        filters.add(v);
                    }
                }
                results.count=filters.size();
                results.values=filters;
            }else{
                results.count=filterList.size();
                results.values=filterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                vbelns = (ArrayList<Vbeln>) results.values;
                notifyDataSetChanged();
        }
    }

    }
