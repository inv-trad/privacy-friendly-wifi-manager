package org.secuso.privacyfriendlywifi.view.viewholder;

import android.net.wifi.WifiConfiguration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlywifi.logic.types.WifiLocationEntry;
import org.secuso.privacyfriendlywifi.view.adapter.DialogWifiListAdapter;

import secuso.org.privacyfriendlywifi.R;

/**
 *
 */
public class DialogWifiItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView ssidTextView;
    private WifiConfiguration conf;
    private DialogWifiListAdapter adapter;

    public DialogWifiItemViewHolder(View itemView, DialogWifiListAdapter adapter) {
        super(itemView);
        this.ssidTextView = (TextView) itemView.findViewById(R.id.wifi_ssid);
        this.adapter = adapter;
        itemView.setOnClickListener(this);
    }

    public void setupItem(WifiConfiguration conf) {
        this.conf = conf;
        this.ssidTextView.setText(conf.SSID);
    }

    @Override
    public void onClick(View v) {
        this.adapter.onListItemClicked(new WifiLocationEntry(conf.SSID, conf.BSSID));
    }
}