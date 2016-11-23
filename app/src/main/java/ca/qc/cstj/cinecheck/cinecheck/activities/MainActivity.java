package ca.qc.cstj.cinecheck.cinecheck.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ca.qc.cstj.cinecheck.cinecheck.R;
import ca.qc.cstj.cinecheck.cinecheck.fragments.CinemaDetailFragment;
import ca.qc.cstj.cinecheck.cinecheck.fragments.CinemaFragment;
import ca.qc.cstj.cinecheck.cinecheck.fragments.CommentaireFragment;
import ca.qc.cstj.cinecheck.cinecheck.fragments.FilmDetailFragment;
import ca.qc.cstj.cinecheck.cinecheck.fragments.FilmFragment;
import ca.qc.cstj.cinecheck.cinecheck.fragments.HoraireFragment;
import ca.qc.cstj.cinecheck.cinecheck.helpers.FragmentTags;
import ca.qc.cstj.cinecheck.cinecheck.models.Cinema;
import ca.qc.cstj.cinecheck.cinecheck.models.Film;
import ca.qc.cstj.cinecheck.cinecheck.models.Horaire;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   CinemaFragment.OnListFragmentInteractionListener,
                   FilmFragment.OnListFragmentInteractionListener,
                   HoraireFragment.OnListFragmentInteractionListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;

    private static int navItemIndex = 0;
    private static FragmentTags CURRENT_TAG = FragmentTags.CINEMAS;

    private Handler handler;
    private ArrayList<String> horaireUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        handler = new Handler();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadCurrentFragment();
    }

    private void loadCurrentFragment() {
        getSupportActionBar().setTitle(getResources().getStringArray(R.array.nav_activities_titles)[CURRENT_TAG.index]);

        Runnable changeFragmentThread = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getCurrentFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.contentframe, fragment, CURRENT_TAG.toString());
                fragmentTransaction.addToBackStack(CURRENT_TAG.toString());
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        handler.post(changeFragmentThread);
        drawer.closeDrawer(GravityCompat.START);
        invalidateOptionsMenu();
    }

    private Fragment getCurrentFragment() {
        switch (navItemIndex){
            case 1:
                return FilmFragment.newInstance(1);
            case 0:
            default:
                return CinemaFragment.newInstance(1);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_manage:
            case R.id.nav_aboot:
            case R.id.nav_contact:
            case R.id.nav_films:
                navItemIndex = 1;
                CURRENT_TAG = FragmentTags.FILMS;
                break;
            case R.id.nav_cinemas:
            default:
                navItemIndex = 0;
                CURRENT_TAG = FragmentTags.CINEMAS;
        }

        loadCurrentFragment();
        return true;
    }

    @Override
    public void onListFragmentInteraction(final Cinema item) {
        if (item.getUrl() == "") {
            return;
        }
        final CinemaDetailFragment fragment = CinemaDetailFragment.newInstance(item.getUrl());

        Runnable changeFragmentThread = new Runnable() {
            @Override
            public void run() {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.contentframe, fragment, CURRENT_TAG.toString());
                fragmentTransaction.addToBackStack(CURRENT_TAG.toString());
                fragmentTransaction.commitAllowingStateLoss();
                Log.d("Horaires Cinema", "START");
                final HoraireFragment fragmenth = HoraireFragment.newInstanceC(1, item.getUrl());

                Runnable changeFragmentThreadH = new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Horaires Cinema", "RUN");
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        //fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        fragmentTransaction.replace(R.id.cinema_horaires, fragmenth, CURRENT_TAG.toString());
                        //fragmentTransaction.addToBackStack(CURRENT_TAG.toString());
                        fragmentTransaction.commitAllowingStateLoss();
                        Log.d("Horaires Cinema", "END");
                    }
                };
                handler.post(changeFragmentThreadH);
            }
        };

        handler.post(changeFragmentThread);
    }

    @Override
    public void onListFragmentInteraction(final Film item) {
        if (item.getUrl() == "") {
            return;
        }
        final Fragment fragment = FilmDetailFragment.newInstance(item.getUrl());

        Runnable changeFragmentThread = new Runnable() {
            @Override
            public void run() {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.contentframe, fragment, CURRENT_TAG.toString());
                fragmentTransaction.addToBackStack(CURRENT_TAG.toString());
                fragmentTransaction.commitAllowingStateLoss();

                final Fragment fragmentc = CommentaireFragment.newInstance(item.getUrl());

                Runnable changeFragmentThreadC = new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Commentaire Film", "RUN");
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        //fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        fragmentTransaction.replace(R.id.fildet_comms, fragmentc, CURRENT_TAG.toString());
                        //fragmentTransaction.addToBackStack(CURRENT_TAG.toString());
                        fragmentTransaction.commitAllowingStateLoss();
                        Log.d("Commentaire Film", "END");
                    }
                };
                handler.post(changeFragmentThreadC);
            }
        };

        handler.post(changeFragmentThread);
    }

    @Override
    public void onListFragmentInteraction(Horaire item) {
        if (item.getFilmUrl() == "") {
            return;
        }
        final Fragment fragment = FilmDetailFragment.newInstance(item.getFilmUrl());

        Runnable changeFragmentThread = new Runnable() {
            @Override
            public void run() {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.contentframe, fragment, CURRENT_TAG.toString());
                fragmentTransaction.addToBackStack(CURRENT_TAG.toString());
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        handler.post(changeFragmentThread);
    }
}
