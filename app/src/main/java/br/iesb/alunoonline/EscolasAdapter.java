package br.iesb.alunoonline;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jefferson on 12/05/2018.
 */

public class EscolasAdapter extends RecyclerView.Adapter<EscolasAdapter.EscolaViewHolder>{
    private Context context;
    private List<Escola> listaEscolas;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, String cod);
    }


    public EscolasAdapter(Context context, List<Escola> listaEscolas, ListItemClickListener listener){
        this.context = context;
        this.listaEscolas = listaEscolas;
        mOnClickListener = listener;
    }



    @Override
    public EscolaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.escola_lista_item, parent, false);
        return new EscolaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EscolaViewHolder holder, int position) {
        Escola escola = listaEscolas.get(position);

        String cod = String.valueOf(escola.codEscola);
        holder.Nome.setText(escola.nome);
        holder.cod.setText(cod);
        /*if (position %2 == 0){
            holder.itemView.setBackgroundColor(Color.rgb(200,155,004));
        }*/
        holder.escola = escola;
    }

    @Override
    public int getItemCount() {
        return listaEscolas.size();
    }

    class EscolaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView Nome;
        public TextView cod;
        public  Escola escola;



        public EscolaViewHolder(View itemView) {
            super(itemView);

            this.Nome = (TextView) itemView.findViewById(R.id.txNome);
            this.cod = (TextView) itemView.findViewById(R.id.txCod);

            itemView.setOnClickListener(this);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            params.setMargins(0,0,0,0);
            itemView.setLayoutParams(params);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            String cod = (String) this.cod.getText();

            mOnClickListener.onListItemClick(clickedPosition, cod);
        }
    }

}
