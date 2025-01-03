package com.sinhvien.orderdrinkapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.sinhvien.orderdrinkapp.DAO.NhanVienDAO;
import com.sinhvien.orderdrinkapp.DTO.ThanhToanDTO;
import com.sinhvien.orderdrinkapp.Fragments.DisplayHomeFragment;
import com.sinhvien.orderdrinkapp.Fragments.DisplayGuideFragment;
import com.sinhvien.orderdrinkapp.Fragments.DisplayStaffFragment;
import com.sinhvien.orderdrinkapp.Fragments.DisplaylienheFragment;
import com.sinhvien.orderdrinkapp.Fragments.DisplaythongtinFragment;
import com.sinhvien.orderdrinkapp.Fragments.DisplayInformationFragment;
import com.sinhvien.orderdrinkapp.R;

import java.util.List;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    MenuItem selectedFeature, selectedManager;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FragmentManager fragmentManager;
    TextView TXT_menu_tennv, TXT_menu_hotennv;
    int maquyen = 0, manv;
    SharedPreferences sharedPreferences;
    BottomNavigationView bot_nav;
    List<ThanhToanDTO> thanhToanDTOList;
    NhanVienDAO nhanVienDAO;


    private BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.nav_home:
                    //hiển thị tương ứng trên navigation
                    FragmentTransaction tranDisplayHome = fragmentManager.beginTransaction();
                    DisplayHomeFragment displayHomeFragment = new DisplayHomeFragment();
                    tranDisplayHome.replace(R.id.contentView,displayHomeFragment);
                    tranDisplayHome.commit();
                    navigationView.setCheckedItem(item.getItemId());
                    return true;


                case R.id.nav_staff:
                    if(maquyen == 1){
                        //hiển thị tương ứng trên navigation
                        FragmentTransaction tranDisplayStaff = fragmentManager.beginTransaction();
                        DisplayStaffFragment displayStaffFragment = new DisplayStaffFragment();
                        tranDisplayStaff.replace(R.id.contentView,displayStaffFragment);
                        tranDisplayStaff.commit();
                        navigationView.setCheckedItem(item.getItemId());
                    }else {
                        Toast.makeText(getApplicationContext(),"Bạn không có quyền truy cập",Toast.LENGTH_SHORT).show();
                    }

                    return true;

                case R.id.nav_information:
                    //hiển thị tương ứng trên navigation
                    FragmentTransaction tranDisplayStatistic = fragmentManager.beginTransaction();
                    DisplayInformationFragment displayInformationFragment = new DisplayInformationFragment();
                    tranDisplayStatistic.replace(R.id.contentView, displayInformationFragment);
                    tranDisplayStatistic.commit();
                    navigationView.setCheckedItem(item.getItemId());
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //region thuộc tính bên view
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view_trangchu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        View view = navigationView.getHeaderView(0);
        TXT_menu_tennv = (TextView) view.findViewById(R.id.txt_menu_tennv);
        //endregion


        //xử lý toolbar và navigation
        setSupportActionBar(toolbar); //tạo toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //tạo nút mở navigation
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar
        ,R.string.opentoggle,R.string.closetoggle){
            @Override
            public void onDrawerOpened(View drawerView) {    super.onDrawerOpened(drawerView);}

            @Override
            public void onDrawerClosed(View drawerView) {    super.onDrawerClosed(drawerView);}
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        //lấy file share prefer
        sharedPreferences = getSharedPreferences("luuquyen", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen",0);

        //hiện thị fragment home mặc định
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction tranDisplayHome = fragmentManager.beginTransaction();
        DisplayHomeFragment displayHomeFragment = new DisplayHomeFragment();
        tranDisplayHome.replace(R.id.contentView, displayHomeFragment);
        tranDisplayHome.commit();
        navigationView.setCheckedItem(R.id.nav_home);

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragmentManager.beginTransaction()
                        .replace(R.id.contentView, new DisplayHomeFragment())
                        .commit();
                break;

            case R.id.huongdansd: // Xử lý cho "Hướng Dẫn Sử Dụng"
                fragmentManager.beginTransaction()
                        .replace(R.id.contentView, new DisplayGuideFragment())
                        .commit();
                break;

            case R.id.thongtin:
                // Xử lý cho "Thông Tin Ứng Dụng"
                fragmentManager.beginTransaction()
                        .replace(R.id.contentView, new DisplaythongtinFragment())
                        .commit();
                break;

            case R.id.lienhe:
                // Xử lý cho "Liên Hệ"
                fragmentManager.beginTransaction()
                        .replace(R.id.contentView, new DisplaylienheFragment())
                        .commit();
                break;

            default:
                return false;
        }
        drawerLayout.closeDrawer(navigationView); // Đóng menu sau khi chọn
        return true;
    }


}