package id.co.sumi.transaku.view.customdialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.sumi.transaku.R;
import id.co.sumi.transaku.model.InventoryCategoryModel;

/**
 * Transaku
 * Author Gebriani
 * on 12, March 2017.
 */

public class CustomSpinnerInventoryCategories extends ArrayAdapter<InventoryCategoryModel> {

    private Context context;

    public CustomSpinnerInventoryCategories(Context context, List<InventoryCategoryModel> objects) {
        super(context, R.layout.r_address, objects);
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomDropdownView(position, convertView, parent);
    }

    @Override
    public int getPosition(InventoryCategoryModel item) {
        return super.getPosition(item);
    }

    @NonNull
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
