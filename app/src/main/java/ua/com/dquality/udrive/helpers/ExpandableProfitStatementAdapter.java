package ua.com.dquality.udrive.helpers;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.viewmodels.ProfitHistoryGroupModel;
import ua.com.dquality.udrive.viewmodels.ProfitHistoryItemModel;
import ua.com.dquality.udrive.viewmodels.ProfitHistoryItemType;
import ua.com.dquality.udrive.viewmodels.ProfitStatementModel;

public class ExpandableProfitStatementAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ProfitStatementModel profitStatementModel;

    public ExpandableProfitStatementAdapter(Context context) {
        this.mContext = context;
        this.profitStatementModel = new ProfitStatementModel();
    }

    public void setGroupItemData(ProfitStatementModel profitStatementModel){
        this.profitStatementModel = profitStatementModel;
        this.notifyDataSetChanged();
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return 0;
//        return this.profitStatementModel.get(groupPosition).Items
//                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ProfitHistoryItemModel itemModel = (ProfitHistoryItemModel) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profit_history_list_item, null);
        }

        TextView itemTime = convertView.findViewById(R.id.item_time);
        String hourAndMinute = new SimpleDateFormat("HH:mm", new Locale(Const.CULTURE)).format(itemModel.Date);
        itemTime.setText(hourAndMinute);

        TextView itemName = convertView.findViewById(R.id.item_name);
        itemName.setText(itemModel.Name);

        TextView itemAmount = convertView.findViewById(R.id.item_amount);

        itemAmount.setTextColor(ContextCompat.getColor(mContext, itemModel.Type == ProfitHistoryItemType.Amount ? (itemModel.Amount < 0 ? R.color.colorError : R.color.colorAccent) : R.color.colorYellow));
        String formattedAmount = String.format("%1$,.2f", itemModel.Amount );
        if(itemModel.Amount > 0){
            formattedAmount = "+ " + formattedAmount;
        }
        itemAmount.setText(formattedAmount);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
//        return this.profitStatementModel.get(groupPosition).Items
//                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return 0;
        //return this.profitStatementModel.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return 0;
        //return this.profitStatementModel.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ProfitHistoryGroupModel groupModel = (ProfitHistoryGroupModel) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profit_history_group_item, null);
        }

        TextView itemHeader = convertView
                .findViewById(R.id.item_header);
        itemHeader.setTypeface(null, Typeface.BOLD);
        String dayAndMonth = new SimpleDateFormat("dd MMMM", new Locale(Const.CULTURE)).format(groupModel.Date);
        itemHeader.setText(dayAndMonth);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}