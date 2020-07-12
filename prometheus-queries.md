### Redirection Stats Panel

Cache hit ratio (Active)
rate(url_redirection_responses_total{cache_hit="true", redirection_status="active"}[10s]) / rate(url_redirection_responses_total[10s])

{{cache_hit}} ratio ({{redirection_status}})
rate(url_redirection_responses_total{cache_hit = "true", redirection_status="active"}[10s]) / rate(url_redirection_responses_total[10s])

Cache miss ratio (Expired)
rate(url_redirection_responses_total{cache_hit = "false", redirection_status="expired"}[10s]) / rate(url_redirection_responses_total[10s])


### Http Request Rates Panel

Id generation
irate(http_requests_total{api="/generate"}[10s])

Shortening
irate(http_requests_total{api="/shorten"}[10s])

Redirection
irate(http_requests_total{api="/{id}"}[10s])


### Http Error Ratio
rate(http_responses_total{http_status=~"4..|5.."}[1m]) / rate(http_responses_total[1m])

Http Success Ratio
rate(http_responses_total{http_status!~"4..|5.."}[1m]) / rate(http_responses_total[1m])