import pandas as pd
from scipy.stats import mannwhitneyu

data = {
    "Algorithm": [
        "Sequential",
        "Sequential",
        "Sequential",
        "Sequential",
        "Sequential",
        "Parallel",
        "Parallel",
        "Parallel",
        "Parallel",
        "Parallel",
    ],
    "Execution Time": [
        69.36,
        68.99,
        72.20,
        77.72,
        73.91,
        10.60,
        10.34,
        10.52,
        10.82,
        10.48,
    ],
}
df = pd.DataFrame(data)

# Separate the execution times into two groups
sequential_times = df[df["Algorithm"] == "Sequential"]["Execution Time"]
parallel_times = df[df["Algorithm"] == "Parallel"]["Execution Time"]

# Perform the Mann-Whitney U test
u_statistic, p_value = mannwhitneyu(sequential_times, parallel_times)

print(f"U statistic: {u_statistic}")
print(f"P-value: {p_value}")
