package com.TNTStudios.lifeoffarce.entity.config;

// Mi clase para guardar las estadísticas de cada mob.
// Es como una ficha técnica que leeré desde un archivo JSON.
public class EntityStats {
    // Defino las variables que quiero controlar.
    // Uso 'double' para los atributos que Minecraft maneja con decimales.
    private double maxHealth;
    private double attackDamage;
    private double movementSpeed;
    private double followRange;
    private boolean naturalSpawn; // Un booleano para decidir si spawnea naturalmente.
    private int spawnWeight;      // Qué tan común es (más alto = más común).
    private int minSpawnCount;    // Mínimo que aparecen en un grupo.
    private int maxSpawnCount;    // Máximo que aparecen en un grupo.

    // Getters para que otras clases puedan leer estos valores.
    // No creo setters porque los valores solo se deben cargar desde el JSON.
    public double getMaxHealth() { return maxHealth; }
    public double getAttackDamage() { return attackDamage; }
    public double getMovementSpeed() { return movementSpeed; }
    public double getFollowRange() { return followRange; }
    public boolean shouldSpawnNaturally() { return naturalSpawn; }
    public int getSpawnWeight() { return spawnWeight; }
    public int getMinSpawnCount() { return minSpawnCount; }
    public int getMaxSpawnCount() { return maxSpawnCount; }
}