package com.pdm.sagaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import java.util.List;

import javax.annotation.Nullable;

public class MessagesActivity extends AppCompatActivity {

    private GroupAdapter adapter;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        rv = findViewById(R.id.rv_Contact);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GroupAdapter();
        rv.setAdapter(adapter);

        verifyAuthentication();

        fetchLastMessage();
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        rv,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Toast.makeText(getApplicationContext(), "Item do Recycler View foi tocado", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(getApplicationContext(), "Item do Recycler View foi tocado com adapter", Toast.LENGTH_SHORT).show();
                            }
                        }
                )
        );

    }

    private void fetchLastMessage() {
        String uid = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore.getInstance().collection("/last-messages")
                .document(uid)
                .collection("contacts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();

                        if (documentChanges != null) {
                            for (DocumentChange doc: documentChanges) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    Contact contact = doc.getDocument().toObject(Contact.class);

                                    adapter.add(new ContactItem(contact));
                                }
                            }
                        }
                    }
                });
    }

    private void verifyAuthentication() {
        if (FirebaseAuth.getInstance().getUid() == null) {
            Intent intent = new Intent(MessagesActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contacts:
                Intent intent = new Intent(MessagesActivity.this, ContactsActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                verifyAuthentication();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ContactItem extends Item<ViewHolder> {

        private final Contact contact;

        private ContactItem(Contact contact) {
            this.contact = contact;
        }


        @Override
        public void bind(@NonNull ViewHolder viewHolder, int position) {
            TextView username = viewHolder.itemView.findViewById(R.id.txt_Contact);
            TextView message = viewHolder.itemView.findViewById(R.id.txt_Message);
            ImageView userPhoto = viewHolder.itemView.findViewById(R.id.img_Contact);

            username.setText(contact.getUsername());
            message.setText(contact.getLastMessage());
            Picasso.get()
                    .load(contact.getPhotoUrl())
                    .into(userPhoto);
        }

        @Override
        public int getLayout() {
            return R.layout.item_user_message;
        }
    }
}
