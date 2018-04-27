package ua.com.dquality.udrive.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.constants.Const;
import ua.com.dquality.udrive.viewmodels.models.ProfitStatementGroupModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitStatementGroupType;
import ua.com.dquality.udrive.viewmodels.models.ProfitStatementItemModel;

public class ExpandableProfitStatementAdapter extends BaseExpandableListAdapter {

    private final Context mContext;
    private List<ProfitStatementGroupModel> mGroupItemData;

    public ExpandableProfitStatementAdapter(Context context) {
        this.mContext = context;
        this.mGroupItemData = new ArrayList<>();
    }

    public void setGroupItemData(List<ProfitStatementGroupModel> groupItemData){
        this.mGroupItemData = groupItemData;
        this.notifyDataSetChanged();
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.mGroupItemData.get(groupPosition).Items
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ProfitStatementItemModel itemModel = (ProfitStatementItemModel) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profit_statement_list_item, null);
        }

        TextView itemTime = convertView.findViewById(R.id.item_time);
        String dateAndTime = new SimpleDateFormat(itemModel.DisplayTime ? Const.SHORT_DATETIME_FORMAT : Const.SHORT_DATE_FORMAT, new Locale(Const.CULTURE)).format(itemModel.Date);
        itemTime.setText(dateAndTime);

        TextView itemName = convertView.findViewById(R.id.item_name);
        itemName.setText(itemModel.Name);

        TextView itemAmount = convertView.findViewById(R.id.item_amount);

        itemAmount.setTextColor(ContextCompat.getColor(mContext, itemModel.Amount < 0 ? R.color.colorError : R.color.colorAccent));
        String formattedAmount = String.format(Const.AMOUNT_FORMAT, itemModel.Amount );
        if(itemModel.Amount > 0){
            formattedAmount = "+ " + formattedAmount;
        }
        itemAmount.setText(formattedAmount);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<ProfitStatementItemModel> childItems = this.mGroupItemData.get(groupPosition).Items;
        return childItems == null? 0:childItems.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mGroupItemData.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mGroupItemData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ProfitStatementGroupModel groupModel = (ProfitStatementGroupModel) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profit_statement_group_item, null);
        }
        TextView groupName = convertView.findViewById(R.id.group_name);
        groupName.setText(groupModel.Name);

        TextView groupAmount = convertView.findViewById(R.id.group_amount);
        groupAmount.setTextColor(ContextCompat.getColor(mContext, groupModel.Amount < 0 ? R.color.colorError : R.color.colorAccent));
        String formattedAmount = String.format(Const.AMOUNT_FORMAT, groupModel.Amount );
        if(groupModel.Amount > 0){
            formattedAmount = "+ " + formattedAmount;
        }
        groupAmount.setText(formattedAmount);

        if(groupModel.Type == ProfitStatementGroupType.Header1)
        {
            groupName.setTypeface(null, Typeface.BOLD);
            groupAmount.setTypeface(null, Typeface.BOLD);
        }

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