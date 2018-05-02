package mrodkiewicz.pl.bakingapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import mrodkiewicz.pl.bakingapp.helper.Config;
import timber.log.Timber;

import static mrodkiewicz.pl.bakingapp.helper.Config.DATABASE_CREATE_INGREDIENT;
import static mrodkiewicz.pl.bakingapp.helper.Config.DATABASE_CREATE_RECIPE;
import static mrodkiewicz.pl.bakingapp.helper.Config.DATABASE_CREATE_STEP;
import static mrodkiewicz.pl.bakingapp.helper.Config.TABLE_INGREDIENT;
import static mrodkiewicz.pl.bakingapp.helper.Config.TABLE_RECIPE;
import static mrodkiewicz.pl.bakingapp.helper.Config.TABLE_STEP;

public class RecipeDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public RecipeDatabaseHelper(Context context){
        super(context, Config.DATABASE_RECIPE,null,Config.DATABASE_VERSION_RECIPE);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Timber.d("RecipeDatabaseHelper onCreate");
        db.execSQL(DATABASE_CREATE_RECIPE);
        db.execSQL(DATABASE_CREATE_STEP);
        db.execSQL(DATABASE_CREATE_INGREDIENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Timber.d("RecipeDatabaseHelper onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENT);
    }
}
