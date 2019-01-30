package com.example.xgramajo.parkme_ids_2018;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirebaseController {

    private static DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    private static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    static void writePatent(String patentString) {
        mRootReference.child("Usuarios").child(currentUser.getUid()).child("Matriculas").push().setValue(patentString);
    }

    static void writeAvaliablePatent(String patent, String direction, String timeEnd) {

        Map newParkingPatent = new HashMap();
        newParkingPatent.put("Matrícula", patent);
        newParkingPatent.put("Localización", direction);
        newParkingPatent.put("HoraFin", timeEnd);

        mRootReference.child("Habilitados").push().updateChildren(newParkingPatent);
    }

    public static void removeAvaliablePatent(final String patent) {

        mRootReference.child("Habilitados").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String prodName = data.child("Matrícula").getValue(String.class);
                    if (prodName.equals(patent)) {
                        mRootReference.child("Habilitados").child(data.getKey()).removeValue();
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}
