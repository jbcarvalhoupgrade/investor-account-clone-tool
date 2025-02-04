# investor-account-clone-tool

This repository contains a tool to generate accounts based on existing account.

## Use
- Generate liquibase scripts
- Generate html content do document in confluence
- Tables supported by the tool:
    - invmgt.investment_account
    - invmgt.bank_account
    - invmgt.account_config
    - invmgt.allocation_config
    - invmgt.cash_sweep_config
    - invmgt.memberization_requirements
    - invmgt.purchase_price_config

## Known issues
- Datagrip export accountConfig fields in a bad format
  - Workaround -> replace fields in the input file
    - collection31to60fee -> collection31to60_fee
    - collection61to90fee -> collection61to90_fee
    - collection91to120fee -> collection91to120_fee
- Datagrip export memberizationRequirements field list as a string
  - Workaround -> Remove skipped double quotes
    - "[{\"name\": \"FUNDS_DESTINATION\"}]" -> [{"name": "FUNDS_DESTINATION"}]
- Datagrip export allocationConfig field list as a string
  - Workaround -> Remove skipped double quotes
    - "[]"-> []
- Script is generated with empty lines when source and replaced attribute are null

## Improvements
- Be able to generate multiple output files or a single file with all tables inserts.
- Be able to pass other variables by parameter like author, ticket-id, ticket description to replace in the liquibase script  

## Attention
- In the table invmgt.investment_account value for field short_name has a constraint by short_name and product_type

## Contribute
- Feel free to contribute and add your scripts to the repo.