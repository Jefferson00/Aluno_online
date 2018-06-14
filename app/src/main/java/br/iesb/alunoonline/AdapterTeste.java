package br.iesb.alunoonline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jefferson on 01/04/2018.
 */

public class AdapterTeste extends RecyclerView.Adapter<AdapterTeste.NumberViewHolder>{

    private  int numItems;

    public AdapterTeste(int numDeItems){numItems = numDeItems;}

    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.layout_list;
        LayoutInflater inflater  = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(AdapterTeste.NumberViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return numItems;
    }

    class NumberViewHolder extends RecyclerView.ViewHolder {

        TextView listItemNomeView , listItemMatriculaView;

        public NumberViewHolder(View itemView) {
            super(itemView);

            listItemNomeView = (TextView) itemView.findViewById(R.id.txNomeList);
            listItemMatriculaView = (TextView) itemView.findViewById(R.id.txMatriculaList);
        }

        void bind(String txnome, String txMatricula){
            listItemNomeView.setText(txnome);
            listItemMatriculaView.setText(txMatricula);
        }

        public void bind(int listIndex) {
            listItemNomeView.setText(String.valueOf(listIndex));
        }
    }
}
