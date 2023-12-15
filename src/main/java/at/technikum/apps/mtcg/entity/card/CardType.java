package at.technikum.apps.mtcg.entity.card;

public enum CardType
{
    WaterGoblin(true, CardElementType.water),
    FireGoblin(true, CardElementType.fire),
    RegularGoblin(true, CardElementType.regular),
    WaterTroll(true, CardElementType.water),
    FireTroll(true, CardElementType.fire),
    RegularTroll(true, CardElementType.regular),
    WaterElf(true, CardElementType.water),
    FireElf(true, CardElementType.fire),
    RegularElf(true, CardElementType.regular),
    WaterSpell(false, CardElementType.water),
    FireSpell(false, CardElementType.fire),
    RegularSpell(false, CardElementType.regular),
    Knight(true, CardElementType.regular),
    Dragon(true, CardElementType.regular),
    Ork(true, CardElementType.regular),
    Kraken(true, CardElementType.regular);

    private final boolean isMonster;
    private final CardElementType elementType;

    CardType(boolean isMonster, CardElementType elementType)
    {
        this.isMonster = isMonster;
        this.elementType = elementType;
    }

    public static CardType createType(String name)
    {
        for (CardType cardType : values())
        {
            if (cardType.name().equalsIgnoreCase(name))
            {
                return cardType;
            }
        }
        throw new IllegalArgumentException("Invalid card type: " + name);
    }

    public boolean isMonster()
    {
        return isMonster;
    }

    public CardElementType getElementType()
    {
        return elementType;
    }
}

