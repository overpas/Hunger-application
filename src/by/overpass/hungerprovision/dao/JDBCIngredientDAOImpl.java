package by.overpass.hungerprovision.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import by.overpass.hungerprovision.model.Ingredient;

public class JDBCIngredientDAOImpl implements IngredientDAO {

	private static final String DATABASE_LOCATION = "localhost/";
	private static final String DATABASE_NAME = "hunger_db";
	private static final String INGREDIENTS_TABLE_NAME = "ingredients";
	private static final String PROVIDERS_TABLE_NAME = "providers";
	private static final String USER_NAME = "root";
	private static final String USER_PASSWORD = "1998";
	private Connection connection = null;

	public JDBCIngredientDAOImpl() throws ClassNotFoundException, SQLException {
		connect();
	}

	public void connect() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		if (connection == null)
			connection = (Connection) DriverManager.getConnection("jdbc:mysql://"
				+ DATABASE_LOCATION + DATABASE_NAME + "?user=" + USER_NAME 
				+ "&password=" + USER_PASSWORD);
	}

	public void disconnect() throws SQLException {
		if (connection != null)
			connection.close();
	}

	@Override
	public boolean insertIngredient(Ingredient ingredient) {
		try {
			boolean shouldCreateNewProvider = shouldCreateNewProvider(ingredient);
			System.out.println("shouldCreateNewProvider = " + shouldCreateNewProvider);
			if (shouldCreateNewProvider) {
				boolean createdNewProvider = createNewProvider(ingredient);
				System.out.println("createdNewProvider = " + createdNewProvider);
				if (!createdNewProvider) {
					System.out.println("!createdNewProvider");
					return false;
				}
			}
			
			System.out.println("ingredient, being inserted: " + ingredient);
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement("INSERT INTO "
				+ INGREDIENTS_TABLE_NAME + "(name, unit, quantity, provider_id, import_date," 
				+ " expiry_date) VALUES (?, ?, ?, (SELECT id FROM " + PROVIDERS_TABLE_NAME 
				+ " WHERE name=?" + " LIMIT 1), ?, ?)");
			ps.setString(1, ingredient.getName());
			ps.setString(2, ingredient.getUnits());
			ps.setDouble(3, ingredient.getQuantity());
			ps.setString(4, ingredient.getProvider());
			ps.setDate(5, ingredient.getImportDate());
			ps.setDate(6, ingredient.getExpiryDate());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public boolean updateIngredient(Ingredient ingredient) {
		try {
			if (shouldCreateNewProvider(ingredient))
				if (!createNewProvider(ingredient)) {
					System.out.println("Couldn't create a new provider.");
					return false;
				}

			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(
				"UPDATE " + INGREDIENTS_TABLE_NAME + " SET name=?, unit=?, quantity=?," 
				+ " provider_id=(SELECT id FROM " + PROVIDERS_TABLE_NAME 
				+ " WHERE name=? LIMIT 1), import_date=?, expiry_date=? WHERE "
				+ "id=?");
			ps.setString(1, ingredient.getName());
			ps.setString(2, ingredient.getUnits());
			ps.setDouble(3, ingredient.getQuantity());
			ps.setString(4, ingredient.getProvider());
			ps.setDate(5, ingredient.getImportDate());
			ps.setDate(6, ingredient.getExpiryDate());
			ps.setInt(7, ingredient.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}
		
		return true;
	}

	@Override
	public boolean deleteIngredient(Ingredient ingredient) {
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement("DELETE FROM "
				+ INGREDIENTS_TABLE_NAME + " WHERE id=?");
			ps.setInt(1, ingredient.getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	@Override
	public List<Ingredient> selectIngredients() throws SQLException {
		List<Ingredient> ingredients = new ArrayList<>();
		Statement statement = (Statement) connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT " + INGREDIENTS_TABLE_NAME 
			+ ".id, " + INGREDIENTS_TABLE_NAME + ".name, "
			+ INGREDIENTS_TABLE_NAME + ".unit, " +INGREDIENTS_TABLE_NAME 
			+ ".quantity, " + PROVIDERS_TABLE_NAME + ".name, " + INGREDIENTS_TABLE_NAME 
			+ ".import_date, " + INGREDIENTS_TABLE_NAME + ".expiry_date FROM "
			+ INGREDIENTS_TABLE_NAME + " INNER JOIN " + PROVIDERS_TABLE_NAME + " WHERE " 
			+ INGREDIENTS_TABLE_NAME + ".provider_id=" + PROVIDERS_TABLE_NAME + ".id");

		if (resultSet != null)
			System.out.println("resultSet != null");
		System.out.println(resultSet);

		Ingredient ingredient = null;
		while (resultSet.next()) {
			ingredient = new Ingredient(resultSet.getInt(INGREDIENTS_TABLE_NAME + ".id"),
					resultSet.getString(INGREDIENTS_TABLE_NAME + ".name"),
					resultSet.getString(INGREDIENTS_TABLE_NAME + ".unit"),
					resultSet.getDouble(INGREDIENTS_TABLE_NAME + ".quantity"),
					resultSet.getString(PROVIDERS_TABLE_NAME + ".name"),
					resultSet.getDate(INGREDIENTS_TABLE_NAME + ".import_date"),
					resultSet.getDate(INGREDIENTS_TABLE_NAME + ".expiry_date"));

			if (ingredient != null)
				System.out.println(ingredient);

			ingredients.add(ingredient);
		}

		resultSet.close();
		statement.close();
		return ingredients;
	}

	private boolean createNewProvider(Ingredient ingredient) {
		try {
			PreparedStatement preparedStatement = (PreparedStatement) connection
				.prepareStatement("INSERT INTO " + PROVIDERS_TABLE_NAME 
						+ "(name) VALUES(?)");
			preparedStatement.setString(1, ingredient.getProvider());
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} catch (SQLException e) {
			System.out.println("Couldn't create a new provider.");
			return false;
		}
		System.out.println("Created a new provider.");

		return true;
	}

	private boolean shouldCreateNewProvider(Ingredient ingredient) throws SQLException {
		Statement statement = (Statement) connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM " + PROVIDERS_TABLE_NAME 
			+ " WHERE name='" + ingredient.getProvider() + "' LIMIT 1");
		
		//if (resultSet.next()) System.out.println("resultSet isn't empty");
		if (resultSet.next()) {
			System.out.println("resultSet = " + resultSet.toString());
			if (resultSet.getInt("id") != 0) {
				System.out.println("resultSet.getString(\"id\") = " + resultSet.getInt("id"));
				if (!resultSet.getString("id").trim().equals("")) {
					resultSet.close();
					statement.close();
					return false;
				}
			} else {
				System.out.println("getString(1) = null!");
			}
		}

		return true;
	}

}
