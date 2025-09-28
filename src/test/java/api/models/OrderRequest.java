package api.models;

import java.util.List;

public class OrderRequest {
    public List<String> ingredients;

    public OrderRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "{ \"ingredients\": " + ingredients.toString() + " }";
    }
}
