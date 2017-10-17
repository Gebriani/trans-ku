package id.co.sumi.transaku.view.customdialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.model.BusinessTypeModel;

/**
 * Created by gebriani on 14/03/17.
 */

public class CustomSpinnerBusinessType extends ArrayAdapter<BusinessTypeModel> {
    private Context context;

    public CustomSpinnerBusinessType(Context context, List<BusinessTypeModel> objects) {
        super(context, R.layout.r_address, objects);
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomDropdownView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomDropdownView(position, convertView, parent);
    }

    public View getCustomDropdownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.r_address, parent, false);
        TextView label = (TextView) row.findViewById(R.id.tvLocation);
        label.setText(getItem(position).getName());
        return row;
    }

}