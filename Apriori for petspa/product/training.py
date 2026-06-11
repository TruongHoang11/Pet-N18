# Luật kết hợp cho đề xuất sản phẩm 
import pandas as pd
import json
import pickle

from mlxtend.frequent_patterns import apriori
from mlxtend.frequent_patterns import association_rules

# ======================================
# CONFIG
# ======================================

DATASET_PATH = "dataset.csv"

OUTPUT_JSON = "association_rules.json"
OUTPUT_MODEL = "model.pkl"

MIN_SUPPORT = 0.01
MIN_CONFIDENCE = 0.10

# ======================================
# LOAD DATA
# ======================================

print("Loading dataset...")

df = pd.read_csv(DATASET_PATH)

required_columns = {"order_id", "product_id"}

if not required_columns.issubset(df.columns):
    raise Exception(
        f"Dataset must contain columns: {required_columns}"
    )

print(f"Rows: {len(df)}")
print(df.head())

# ======================================
# CREATE BASKET
# ======================================

print("\nCreating basket...")

basket = (
    df.groupby(["order_id", "product_id"])
      .size()
      .unstack(fill_value=0)
)

# Pandas 3.x compatible
basket = (basket > 0)

print(f"Basket shape: {basket.shape}")

# ======================================
# APRIORI
# ======================================

print("\nTraining Apriori...")

frequent_itemsets = apriori(
    basket,
    min_support=MIN_SUPPORT,
    use_colnames=True
)

print(
    f"Frequent itemsets found: {len(frequent_itemsets)}"
)

if frequent_itemsets.empty:
    raise Exception(
        "No frequent itemsets found. "
        "Try reducing MIN_SUPPORT."
    )

# ======================================
# RULE GENERATION
# ======================================

print("\nGenerating rules...")

rules = association_rules(
    frequent_itemsets,
    metric="confidence",
    min_threshold=MIN_CONFIDENCE
)

if rules.empty:
    raise Exception(
        "No association rules found. "
        "Try reducing MIN_CONFIDENCE."
    )

rules = rules.sort_values(
    by=["confidence", "lift"],
    ascending=False
)

print(f"Rules found: {len(rules)}")

# ======================================
# EXPORT JSON FOR SPRING BOOT
# ======================================

print("\nExporting JSON...")

json_rules = []

for _, row in rules.iterrows():

    json_rules.append({
        "antecedent": sorted(
            [int(x) for x in row["antecedents"]]
        ),

        "consequent": sorted(
            [int(x) for x in row["consequents"]]
        ),

        "confidence": round(
            float(row["confidence"]),
            6
        ),

        "support": round(
            float(row["support"]),
            6
        ),

        "lift": round(
            float(row["lift"]),
            6
        )
    })

with open(
    OUTPUT_JSON,
    "w",
    encoding="utf-8"
) as f:

    json.dump(
        json_rules,
        f,
        ensure_ascii=False,
        indent=4
    )

print(
    f"JSON saved -> {OUTPUT_JSON}"
)

# ======================================
# SAVE MODEL
# ======================================

print("\nSaving model...")

model = {
    "frequent_itemsets": frequent_itemsets,
    "rules": rules,
    "min_support": MIN_SUPPORT,
    "min_confidence": MIN_CONFIDENCE
}

with open(OUTPUT_MODEL, "wb") as f:
    pickle.dump(model, f)
    
print( f"Model saved -> {OUTPUT_MODEL}" )

# ======================================
# PREVIEW
# ======================================

print("\nTop 10 rules:\n")

for rule in json_rules[:10]:
    print(rule)

print("\nTraining completed successfully.")