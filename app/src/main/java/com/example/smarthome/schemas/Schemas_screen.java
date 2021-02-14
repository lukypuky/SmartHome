package com.example.smarthome.schemas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smarthome.main.Main_screen;
import com.example.smarthome.R;
import com.example.smarthome.profile.Profile_screen;
import com.example.smarthome.settings.Settings_screen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Schemas_screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    //menu
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    //pridanie schemy
    private FloatingActionButton addSchema;
    private AlertDialog.Builder addSchemaDialog;
    private AlertDialog dialog;
    private EditText schemaName;
    private Button saveSchema, unsaveSchema;

    //zoznam schem
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter; // bridge medzi datami a recycler view
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<SchemaItem> schemaList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schemas_screen);

        createSchemaList();

        //tlacidlo na pridanie novej schemy
        addSchema= (FloatingActionButton) findViewById(R.id.addSchema);
        addSchema.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addSchemaDialog();
            }
        });

        setRecyclerView();
        setNavigationView();
    }

    //plocha pre schemy v domacnosti
    public void  setRecyclerView()
    {
        mRecyclerView = findViewById(R.id.schemaRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new SchemaAdapter(schemaList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        new ItemTouchHelper(schemaToRemove).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    //bocny navigacny panel
    public void setNavigationView()
    {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView.bringToFront(); //pri kliknuti na menu nam menu nezmizne
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //umozni nam klikat v menu
        navigationView.setNavigationItemSelectedListener(this);

        //pri spusteni appky bude zakliknuta defaultne schema screena
        navigationView.setCheckedItem(R.id.schema);
    }

    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer((GravityCompat.START));
        }

        else
        {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.mainScreen:
                Intent main_intent = new Intent(Schemas_screen.this, Main_screen.class);
                startActivity(main_intent);
                break;
            case R.id.profile:
                Intent profile_intent = new Intent(Schemas_screen.this, Profile_screen.class);
                startActivity(profile_intent);
                break;
            case R.id.schema:
                break;
            case R.id.settings:
                Intent settings_intent = new Intent(Schemas_screen.this, Settings_screen.class);
                startActivity(settings_intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void createSchemaList()
    {
        schemaList = new ArrayList<>();
    }

    //prida schemu na index 0 v arrayliste
    public void insertSchema(EditText schemaName)
    {
        String name = schemaName.getText().toString();
        int position = 0;

        schemaList.add(position, new SchemaItem(R.drawable.schemas_icon,name));
        mAdapter.notifyItemInserted(position);
    }

    //metoda na pridanie novej schemy v domacnosti
    public void addSchemaDialog()
    {
        addSchemaDialog = new AlertDialog.Builder(Schemas_screen.this);
        View contactPopupView = getLayoutInflater().inflate(R.layout.activity_add_schema_popup, null);

        schemaName = (EditText) contactPopupView.findViewById(R.id.schemaName);
        saveSchema = (Button) contactPopupView.findViewById(R.id.saveSchemaButton);
        unsaveSchema = (Button) contactPopupView.findViewById(R.id.unsaveSchemaButton);

        addSchemaDialog.setView(contactPopupView);
        dialog = addSchemaDialog.create();
        dialog.show();

        //potvrdenie pridania schemy
        saveSchema.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //ak sa nevyplnil nazov schemy -> message
                if (schemaName.getText().toString().isEmpty())
                {
                    Toast.makeText(Schemas_screen.this, "Zadajte názov pre schému", Toast.LENGTH_SHORT).show();
                }

                //ak je vsetko vyplnene, pridaj schemu do arraylistu
                else
                {
                    insertSchema(schemaName);
                    Toast.makeText(Schemas_screen.this, "Schéma pridaná", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        //zrusenie pridania schemy
        unsaveSchema.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
    }

    //mazanie schem (with swap right)
    ItemTouchHelper.SimpleCallback schemaToRemove = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT)
    {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
        {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
        {
            //builder na potvrdenie zmazania
            AlertDialog.Builder builder = new AlertDialog.Builder(Schemas_screen.this);
            builder.setCancelable(true);
            builder.setMessage("Naozaj chcete odstrániť túto schému '" + schemaList.get(viewHolder.getAdapterPosition()).getText().toUpperCase() + "' ?");
            builder.setPositiveButton("Áno", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    schemaList.remove(viewHolder.getAdapterPosition());
                    mAdapter.notifyDataSetChanged();
                }
            });

            builder.setNegativeButton("Nie", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                    mAdapter.notifyDataSetChanged();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };
}