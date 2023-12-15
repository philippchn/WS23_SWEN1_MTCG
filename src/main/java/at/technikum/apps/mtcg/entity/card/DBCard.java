package at.technikum.apps.mtcg.entity.card;

public record DBCard(String id, String name, float damage, boolean isMonster, String elementType) {
}
