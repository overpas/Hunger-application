package by.overpass.hungerprovision.dao;

import java.sql.SQLException;
import java.util.List;

import by.overpass.hungerprovision.model.Ingredient;

public interface IngredientDAO {
	public boolean connect() throws SQLException, ClassNotFoundException;
	public boolean disconnect() throws SQLException;
	public boolean insertIngredient(Ingredient ingredient);
	public boolean updateIngredient(Ingredient ingredient);
	public boolean deleteIngredient(Ingredient ingredient);
	public List<Ingredient> selectIngredients() throws SQLException;
}
