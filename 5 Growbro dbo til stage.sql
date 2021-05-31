use [GrowBroDWH]

truncate table stage.DimDrivhus
insert into stage.DimDrivhus
([DrivhusID]
      ,[Navn])
SELECT [DrivhusID]
      ,[Navn]
FROM dbo.Drivhus

truncate table stage.DimEjer
insert into stage.DimEjer
([UserID]
      ,[Username])
SELECT [UserID]
      ,[Username]
FROM dbo.Ejer

truncate table stage.DimPlante
insert into stage.DimPlante
([PlanteID]
      ,[Navn])
SELECT [PlanteID]
      ,[Navn]
FROM dbo.Plante

truncate table stage.FactManagement
insert into stage.FactManagement
([UserID]
      ,[DrivhusID]
      ,[PlanteID]
      ,[Temperatur]
      ,[CO2]
      ,[Fugtighed]
      ,[Time])
SELECT e.[UserID]
      ,p.[DrivhusID]
      ,[PlanteID]
      ,[Temperatur]
      ,[CO2]
      ,[Fugtighed]
      ,dh.[Time]
FROM dbo.Drivhus dh
inner join dbo.ejer e
on dh.UserID = e.UserID
inner join dbo.Plante p
on p.DrivhusID = dh.DrivhusID;