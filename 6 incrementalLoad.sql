declare @lastLoadDate DateTime
declare @newLoadDate DateTime
declare @futureDate DateTime
set @newLoadDate = convert(char(25), getDate(), 113)
set @futureDate = '9999-12-31 23:59:59'
--DimEjer
set @lastLoadDate = (select max(LoadDate) from etl.logUpdate where [table] = 'DimEjer')

insert into edwh.DimEjer(UserID,Username,validFrom,validTo) select UserID,Username,@newLoadDate,@futureDate from stage.DimEjer --add new data
where UserID in (select UserID from stage.DimEjer except select UserID from edwh.DimEjer where validTo = '9999-12-31 23:59:59')

select UserID, Username into #tmp from stage.DimEjer except select UserID,Username from edwh.DimEjer where validTo= '9999-12-31 23:59:59'--edit changed data
except select UserID,Username from stage.DimEjer where UserID in (select UserID from stage.DimEjer except select UserID from edwh.DimEjer where validTo = '9999-12-31 23:59:59')

insert into edwh.DimEjer(UserID,Username,validFrom,validTo) select UserID,Username,@newLoadDate,@futureDate from #tmp
update edwh.DimEjer set validTo=@newLoadDate-1
where UserID in (select UserID from #tmp) and edwh.DimEjer.validFrom<@newLoadDate
drop table if exists #tmp


update edwh.DimEjer set validTo=@newLoadDate-1 where UserID in (select UserID from edwh.DimEjer where UserID in (select UserID from edwh.DimEjer except select UserID from stage.DimEjer)) -- delete deleted data
and 
validTo = '9999-12-31 23:59:59'

insert into etl.logUpdate ([Table],LoadDate) values('DimEjer',@newLoadDate)





--DimPlante
set @lastLoadDate = (select max(LoadDate) from etl.logUpdate where [table] = 'DimPlante')

insert into edwh.DimPlante(PlanteID,Navn,validFrom,validTo) select PlanteID,Navn,@newLoadDate,@futureDate
from stage.DimPlante --add new data
where PlanteID in (select PlanteID from stage.DimPlante except select PlanteID from edwh.DimPlante where validTo = '9999-12-31 23:59:59')

select PlanteID,Navn into #tmps from stage.DimPlante except select PlanteID,Navn from edwh.DimPlante where validTo= '9999-12-31 23:59:59'--edit changed data
except select PlanteID,Navn from stage.DimPlante where PlanteID in (select PlanteID from stage.DimPlante except select PlanteID from edwh.DimPlante where validTo = '9999-12-31 23:59:59')

insert into edwh.DimPlante(PlanteID,Navn,validFrom,validTo) select PlanteID,Navn,@newLoadDate,@futureDate from #tmps
update edwh.DimPlante set validTo=@newLoadDate-1
where PlanteID in (select PLanteID from #tmps) and edwh.DimPlante.validFrom<@newLoadDate
drop table if exists #tmps


update edwh.DimPlante set validTo=@newLoadDate-1 where PlanteID in (select PlanteID from edwh.DimPlante where PlanteID in (select PlanteID from edwh.DimPlante except select PlanteID from stage.DimPlante)) -- delete deleted data
and 
validTo = '9999-12-31 23:59:59'

insert into etl.logUpdate ([Table],LoadDate) values('DimPlante',@newLoadDate)



--DimDrivhus
set @lastLoadDate = (select max(LoadDate) from etl.logUpdate where [table] = 'DimDrivhus')

insert into edwh.DimDrivhus(DrivhusID,Navn,validFrom,validTo) select DrivhusID,Navn,@newLoadDate,@futureDate
from stage.DimDrivhus --add new data
where DrivhusID in (select DrivhusID from stage.DimDrivhus except select DrivhusID from edwh.DimDrivhus where validTo = '9999-12-31 23:59:59')

select DrivhusID,Navn into #temp from stage.DimDrivhus except select DrivhusID,Navn from edwh.DimDrivhus where validTo= '9999-12-31 23:59:59'--edit changed data
except select DrivhusID,Navn from stage.DimDrivhus where DrivhusID in (select DrivhusID from stage.DimDrivhus except select DrivhusID from edwh.DimDrivhus where validTo = '9999-12-31 23:59:59')

insert into edwh.DimDrivhus(DrivhusID,Navn,validFrom,validTo) select DrivhusID,Navn,@newLoadDate,@futureDate from #temp
update edwh.DimDrivhus set validTo=@newLoadDate-1
where DrivhusID in (select DrivhusID from #temp) and edwh.DimDrivhus.validFrom<@newLoadDate
drop table if exists #temp


update edwh.DimDrivhus set validTo=@newLoadDate-1 where DrivhusID in (select drivhusID from edwh.DimPlante where DrivhusID in (select DrivhusID from edwh.DimDrivhus except select DrivhusID from stage.DimDrivhus)) -- delete deleted data
and 
validTo = '9999-12-31 23:59:59'

insert into etl.logUpdate ([Table],LoadDate) values('DimDrivhus',@newLoadDate)



--FactManagement

insert into edwh.FactManagement(DH_ID,U_ID,P_ID,D_ID,Temperatur,CO2,Fugtighed) select DH.DH_ID,U.U_ID,P.P_ID,D.D_ID,F.Temperatur,F.CO2,F.Fugtighed
from stage.FactManagement as F
left join edwh.DimEjer as U on U.UserID = F.UserID
left join edwh.DimDrivhus as DH on DH.DrivhusID = F.DrivhusID
left join edwh.DimPlante as P on P.PlanteID = F.PlanteID
left join edwh.DimDate as D on D.Date = F.[Time]
where U.validTo = '9999-12-31 23:59:59' and DH.validTo = '9999-12-31 23:59:59' and P.validTo = '9999-12-31 23:59:59';


