package com.example.datn_md03_ungdungmuabangiaysneakzone.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.HoaDonAdapter_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.Demo.HoaDon_Demo;
import com.example.datn_md03_ungdungmuabangiaysneakzone.R;

import java.util.ArrayList;
import java.util.List;


public class Fragment_ChoXacNhan extends Fragment {

    private RecyclerView recyclerView;
    private HoaDonAdapter_Demo hoaDonAdapter;
    private List<HoaDon_Demo> hoaDonList;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_ChoXacNhan() {
        // Required empty public constructor
    }

    public static Fragment_ChoXacNhan newInstance(String param1, String param2) {
        Fragment_ChoXacNhan fragment = new Fragment_ChoXacNhan();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cho_xac_nhan, container, false);
        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.rcv_wait);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        hoaDonList = new ArrayList<>();
        hoaDonList.add(new HoaDon_Demo("Nguyễn Văn A", "Thị trấn Trạm trôi, Hoài Đức, Hà Nội", "0123456789", 3, 1500000, "2024-10-22"));
        hoaDonList.add(new HoaDon_Demo("Trần Thị B", "TP. Hồ Chí Minh", "0987654321", 2, 2000000, "2024-10-21"));
        hoaDonList.add(new HoaDon_Demo("Nguyễn Văn A", "Thị trấn Trạm trôi, Hoài Đức, Hà Nội", "0123456789", 3, 1500000, "2024-10-22"));
        hoaDonList.add(new HoaDon_Demo("Trần Thị B", "TP. Hồ Chí Minh", "0987654321", 2, 2000000, "2024-10-21"));
        hoaDonList.add(new HoaDon_Demo("Nguyễn Văn A", "Thị trấn Trạm trôi, Hoài Đức, Hà Nội", "0123456789", 3, 1500000, "2024-10-22"));
        hoaDonList.add(new HoaDon_Demo("Trần Thị B", "TP. Hồ Chí Minh", "0987654321", 2, 2000000, "2024-10-21"));

        hoaDonAdapter = new HoaDonAdapter_Demo(getContext(), hoaDonList);
        recyclerView.setAdapter(hoaDonAdapter);
        return view;
    }
}