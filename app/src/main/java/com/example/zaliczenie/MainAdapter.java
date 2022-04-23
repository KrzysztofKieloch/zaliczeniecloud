package com.example.zaliczenie;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myVievHolder>{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myVievHolder holder, int position, @NonNull MainModel mainModel) {
        holder.marka.setText(mainModel.getMarka());
        holder.model.setText(mainModel.getModel());

        Glide.with(holder.img.getContext())
                .load(mainModel.getPurl())
                .placeholder(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.storage.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

        holder.btnEdit.setOnClickListener(view -> {
            final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                    .setContentHolder(new ViewHolder(R.layout.update_popup))
                    .setExpanded(true,1200)
                    .create();

            View v = dialogPlus.getHolderView();

            EditText model = v.findViewById(R.id.txtModel);
            EditText marka = v.findViewById(R.id.txtMarka);
            EditText purl = v.findViewById(R.id.txtUrl);

            Button btnUpdate = v.findViewById(R.id.btnUpdate);

            model.setText(mainModel.getModel());
            marka.setText(mainModel.getMarka());
            purl.setText(mainModel.getPurl());

            dialogPlus.show();

            btnUpdate.setOnClickListener(view1 -> {
                Map<String,Object> map = new HashMap<>();
                map.put("model",model.getText().toString());
                map.put("marka",marka.getText().toString());
                map.put("purl",purl.getText().toString());

                FirebaseDatabase.getInstance().getReference().child("Urządzenia")
                        .child(Objects.requireNonNull(getRef(position).getKey())).updateChildren(map).addOnSuccessListener(unused -> {
                            Toast.makeText(holder.model.getContext(), "Zauktaulizowano pomyślnie.",Toast.LENGTH_SHORT).show();
                            dialogPlus.dismiss();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(holder.model.getContext(), "Odrzucono zmiany.",Toast.LENGTH_SHORT).show();
                            dialogPlus.dismiss();
                        });
            });
        });
        holder.btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.model.getContext());
            builder.setTitle("Jesteś pewny?");
            builder.setMessage("Proces jest nieodwracalny");

            builder.setPositiveButton("Usuń", (dialogInterface, i) -> FirebaseDatabase.getInstance().getReference().child("Urządzenia")
                    .child(Objects.requireNonNull(getRef(position).getKey())).removeValue());
            builder.setNegativeButton("Cofnij", (dialogInterface, i) -> Toast.makeText(holder.model.getContext(), "Odrzucono.",Toast.LENGTH_SHORT).show());
            builder.show();
        });
    }

    @NonNull
    @Override
    public myVievHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent, false);
        return new myVievHolder(view);
    }

    static class myVievHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView marka,model;

        Button btnEdit,btnDelete;

        public myVievHolder(@NonNull View itemView) {
            super(itemView);

            img = (CircleImageView)itemView.findViewById(R.id.img1);
            marka = (TextView)itemView.findViewById(R.id.markatext);
            model = (TextView)itemView.findViewById(R.id.modeltext);

            btnEdit = (Button)itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }
}
