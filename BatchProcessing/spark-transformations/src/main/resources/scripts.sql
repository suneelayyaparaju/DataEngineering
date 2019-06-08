## Find a time difference between orders per CustomerID

Approach:1
----------
SELECT
  CustomerID,
  AvgLag = AVG(CONVERT(decimal(7,2), DATEDIFF(DAY, PriorDate, OrderDate)))
FROM
(
  SELECT CustomerID, OrderDate, PriorDate = LAG(OrderDate,1)
    OVER (PARTITION BY CustomerID ORDER BY OrderDate)
  FROM Orders
) AS lagged
GROUP BY CustomerID;

Approach:2
----------
SELECT
  CustomerID,
  AvgLag = CASE WHEN COUNT(*) > 1 THEN
                    CONVERT(decimal(7,2),
                            DATEDIFF(day, MIN(OrderDate), MAX(OrderDate)))
                    / CONVERT(decimal(7,2), COUNT(*) - 1)
                ELSE NULL
           END
FROM Orders
GROUP BY CustomerID;

Approach:3
----------
select CustomerID,
    cast(DATEDIFF(dd, min(OrderDate), max(OrderDate)) as decimal) / (count(*) - 1) as [avgDayDelta], count(*)
from Orders
group by CustomerID
having count(*) > 1