package com.sinhvien.orderdrinkapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sinhvien.orderdrinkapp.Activities.FeedbackActivity;
import com.sinhvien.orderdrinkapp.Activities.ProductDetailActivity;
import com.sinhvien.orderdrinkapp.R;

public class DisplaylienheFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Gắn layout huong_dan_su_dung_layout
        View view = inflater.inflate(R.layout.lienhe, container, false);

        // Tìm nút "Quay Lại"
        ImageView IMG_lienhe_backbtn = view.findViewById(R.id.img_lienhe_back);
        ImageView IMG_lienhe_fb = view.findViewById(R.id.imageView1);
        ImageView IMG_lienhe_ytb = view.findViewById(R.id.imageView2);
        ImageView IMG_lienhe_ig = view.findViewById(R.id.imageView3);


        Button btnphanhoi = view.findViewById(R.id.btnphanhoi);

        // Xử lý sự kiện click
        IMG_lienhe_fb.setOnClickListener(v -> openProductDetail("https://www.facebook.com/hks.hili"));
        IMG_lienhe_ytb.setOnClickListener(v -> openProductDetail("https://www.youtube.com/@Hi%E1%BA%BFuHuy-g8m"));
        IMG_lienhe_ig.setOnClickListener(v -> openProductDetail("https://www.instagram.com/hieu_huy_3010/"));
        IMG_lienhe_backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay về Fragment Home
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentView, new DisplayHomeFragment())
                        .commit();
            }
        });

        btnphanhoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
    private void openProductDetail(String url) {
        Intent intent = new Intent(requireActivity(), ProductDetailActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
