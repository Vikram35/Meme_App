package com.choudhary.memebook;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
     ArrayList<Model> modelArrayList;

    public Adapter(Context context, ArrayList<Model> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String url = modelArrayList.get(position).getUrl();
          holder.setImage(url);
          holder.button.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent sharing = new Intent (Intent.ACTION_SEND);
                  sharing.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  sharing.setType("text/plain");
                  String subject = "Hey Man just look at this coll meme click the link " +url;
                  sharing.putExtra(Intent.EXTRA_TEXT,subject);
                  context.startActivity(Intent.createChooser(sharing,"Shring using"));
              }
          });

          holder.buttonDownload.setOnClickListener(new View.OnClickListener() {
              @Override


              public void onClick(View view) {

                      
                        downloadImage(url);
              }
          });

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        Button button,buttonDownload;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            button = itemView.findViewById(R.id.button);
            buttonDownload = itemView.findViewById(R.id.btn_download);
        }

        void setImage(String link){
            Glide.with(context).load(link).into(imageView);
        }
    }

         void downloadImage(String imageURL){

         String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "memebook" + "/";
         final File dir = new File(dirPath);
         final String fileName = imageURL.substring(imageURL.lastIndexOf('/') + 1);
         Glide.with(context)
                     .load(imageURL)
                     .into(new CustomTarget<Drawable>() {
                         @Override
                         public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                             Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
                             Toast.makeText(context, "Saving Image...", Toast.LENGTH_SHORT).show();
                             saveImage(bitmap, dir, fileName);
                         }

                         @Override
                         public void onLoadCleared(@Nullable Drawable placeholder) {

                         }

                         @Override
                         public void onLoadFailed(@Nullable Drawable errorDrawable) {
                             super.onLoadFailed(errorDrawable);

                             Toast.makeText(context, "Failed to Download Image! Please try again later.", Toast.LENGTH_SHORT).show();
                         }
                     });

         }


    private void saveImage(Bitmap image, File storageDir, String imageFileName) {

        boolean successDirCreated = false;
        if (!storageDir.exists()) {
            successDirCreated = storageDir.mkdir();
        }

        successDirCreated = true;
        if (successDirCreated) {
            File imageFile = new File(storageDir, imageFileName);
            String savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
                Toast.makeText(context, "Image Saved!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "Error while saving image!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }else{
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }
    }



}
