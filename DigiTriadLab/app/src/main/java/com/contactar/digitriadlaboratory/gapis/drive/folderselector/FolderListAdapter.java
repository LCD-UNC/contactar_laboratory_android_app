package com.contactar.digitriadlaboratory.gapis.drive.folderselector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.services.drive.model.File;
import com.contactar.digitriadlaboratory.R;

import java.util.List;

public class FolderListAdapter extends RecyclerView.Adapter<FolderListAdapter.FolderViewHolder>  {
    private final Context context;
    private final LayoutInflater inflater;
    private List<File> folders;

    public FolderListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.folder_list_item, parent, false);
        return new FolderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        if(folders != null) {
            File currentFolder = folders.get(position);
            holder.folderItemView.setText(currentFolder.getName());
            holder.folder = currentFolder;
        } else {
            holder.folderItemView.setText(R.string.loading_data);
        }
    }

    @Override
    public int getItemCount() {
        if (folders != null)
            return folders.size();
        else
            return 0;
    }

    public void setFolders(List<File> files) {
        folders = files;
        notifyDataSetChanged();
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder{

        public final TextView folderItemView;
        public final ConstraintLayout parentLayout;
        public File folder;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            folderItemView = itemView.findViewById(R.id.label);
            parentLayout = itemView.findViewById(R.id.folder_list_item);
        }
    }
}
